package com.hypersoft.pzlayout.utils

import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.hypersoft.pzlayout.interfaces.Area
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

object MatrixUtils {

    // This is a temporary storage for matrix values and a reusable matrix to optimize performance.
    private val sMatrixValues = FloatArray(9)
    private val sTempMatrix = Matrix()

    /**
     * This function calculates and returns the scale of the matrix.
     * It uses the MSCALE_X and MSKEW_Y matrix values to compute the scale factor.
     */
    @JvmStatic
    fun getMatrixScale(matrix: Matrix): Float {
        return sqrt(
            getMatrixValue(matrix, Matrix.MSCALE_X).toDouble().pow(2.0) +
                    getMatrixValue(matrix, Matrix.MSKEW_Y).toDouble().pow(2.0)
        ).toFloat()
    }

    /**
     * This function calculates and returns the rotation angle (in degrees) of the matrix.
     * It uses MSKEW_X and MSCALE_X matrix values to compute the angle.
     */
    @JvmStatic
    fun getMatrixAngle(matrix: Matrix): Float {
        return -(atan2(
            getMatrixValue(matrix, Matrix.MSKEW_X).toDouble(),
            getMatrixValue(matrix, Matrix.MSCALE_X).toDouble()
        ) * (180 / Math.PI)).toFloat()
    }

    /**
     * Helper function to extract a specific value (like scale or skew) from the matrix.
     */
    private fun getMatrixValue(matrix: Matrix, valueIndex: Int): Float {
        matrix.getValues(sMatrixValues)
        return sMatrixValues[valueIndex]
    }

    /**
     * This function calculates the minimum scale factor that can fit the PuzzlePiece within its area,
     * accounting for any rotation applied to the piece.
     */
    @JvmStatic
    fun getMinMatrixScale(piece: PuzzlePiece?): Float {
        if (piece != null) {
            sTempMatrix.reset()
            // Apply reverse rotation to undo the piece's rotation.
            sTempMatrix.setRotate(-piece.matrixAngle)

            // Get the corners of the area the piece fits into (unrotated).
            val unrotatedCropBoundsCorners = piece.area.areaRect.let { getCornersFromRect(it) }

            // Map the points with the reverse rotation matrix.
            sTempMatrix.mapPoints(unrotatedCropBoundsCorners)

            // Convert the corner points to a rectangle.
            val unrotatedCropRect = trapToRect(unrotatedCropBoundsCorners)

            // Calculate the maximum scale needed to fit the piece's width or height.
            return max(
                (unrotatedCropRect.width() / piece.width).toDouble(),
                (unrotatedCropRect.height() / piece.height).toDouble()
            ).toFloat()
        }
        return 1f
    }

    /**
     * This function checks if the image of the PuzzlePiece fully contains its borders after being rotated.
     * It compares the image and border's bounds to determine this.
     */
    @JvmStatic
    fun judgeIsImageContainsBorder(piece: PuzzlePiece, rotateDegrees: Float): Boolean {
        sTempMatrix.reset()
        // Apply reverse rotation to undo the piece's rotation.
        sTempMatrix.setRotate(-rotateDegrees)

        val unrotatedWrapperCorner = FloatArray(8)
        val unrotateBorderCorner = FloatArray(8)
        // Map the points of the drawable and the area rectangle (unrotated).
        sTempMatrix.mapPoints(unrotatedWrapperCorner, piece.currentDrawablePoints)
        sTempMatrix.mapPoints(unrotateBorderCorner, piece.area.areaRect.let { getCornersFromRect(it) })

        // Check if the image completely contains the border.
        return trapToRect(unrotatedWrapperCorner).contains(trapToRect(unrotateBorderCorner))
    }

    /**
     * This function calculates the distances (indents) between the image and its containing area on all sides.
     * It handles any rotation applied to the image before making the calculation.
     */
    @JvmStatic
    fun calculateImageIndents(piece: PuzzlePiece?): FloatArray {
        if (piece == null) return floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

        sTempMatrix.reset()
        // Undo the rotation to align the image correctly.
        sTempMatrix.setRotate(-piece.matrixAngle)

        // Copy the drawable points and calculate unrotated corner points of the image and its area.
        val currentImageCorners = piece.currentDrawablePoints
        val unrotatedImageCorners = currentImageCorners.copyOf(currentImageCorners.size)
        val unrotatedCropBoundsCorners = piece.area.areaRect.let { getCornersFromRect(it) }

        sTempMatrix.mapPoints(unrotatedImageCorners)
        sTempMatrix.mapPoints(unrotatedCropBoundsCorners)

        // Convert the mapped points to rectangles.
        val unrotatedImageRect = trapToRect(unrotatedImageCorners)
        val unrotatedCropRect = trapToRect(unrotatedCropBoundsCorners)

        // Calculate how far the image exceeds or falls short on all sides.
        val deltaLeft = unrotatedImageRect.left - unrotatedCropRect.left
        val deltaTop = unrotatedImageRect.top - unrotatedCropRect.top
        val deltaRight = unrotatedImageRect.right - unrotatedCropRect.right
        val deltaBottom = unrotatedImageRect.bottom - unrotatedCropRect.bottom

        // Store the indents (if the image is beyond the bounds).
        val indents = FloatArray(4)
        indents[0] = if (deltaLeft > 0) deltaLeft else 0f
        indents[1] = if (deltaTop > 0) deltaTop else 0f
        indents[2] = if (deltaRight < 0) deltaRight else 0f
        indents[3] = if (deltaBottom < 0) deltaBottom else 0f

        // Rotate the indents back to match the image's original orientation.
        sTempMatrix.reset()
        sTempMatrix.setRotate(piece.matrixAngle)
        sTempMatrix.mapPoints(indents)

        return indents
    }

    /**
     * Converts a set of points (trapezoid) into a rectangle that contains all the points.
     */
    private fun trapToRect(array: FloatArray): RectF {
        val r = RectF(
            Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY,
            Float.NEGATIVE_INFINITY
        )
        var i = 1
        while (i < array.size) {
            val x = Math.round(array[i - 1] * 10) / 10f
            val y = Math.round(array[i] * 10) / 10f
            r.left = if (x < r.left) x else r.left
            r.top = if (y < r.top) y else r.top
            r.right = if (x > r.right) x else r.right
            r.bottom = if (y > r.bottom) y else r.bottom
            i += 2
        }
        r.sort()
        return r
    }

    /**
     * Extracts the four corners of a given rectangle.
     */
    private fun getCornersFromRect(r: RectF): FloatArray {
        return floatArrayOf(
            r.left, r.top, r.right, r.top, r.right, r.bottom, r.left, r.bottom
        )
    }

    /**
     * This function generates a matrix that centers and scales a drawable within the specified area.
     * It adds an optional extra size for scaling.
     */
    @JvmStatic
    fun generateMatrix(piece: PuzzlePiece, extra: Float): Matrix {
        return generateMatrix(piece.area, piece.drawable, extra)
    }

    /**
     * This function creates a transformation matrix that fits a drawable (image) into the specified area.
     * It centers and scales the drawable according to its dimensions and the area's dimensions.
     */
    @JvmStatic
    fun generateMatrix(area: Area, drawable: Drawable, extraSize: Float): Matrix {
        return generateCenterCropMatrix(
            area, drawable.intrinsicWidth,
            drawable.intrinsicHeight, extraSize
        )
    }

    /**
     * This function generates a matrix for scaling and centering a drawable in an area using a "center crop" approach.
     */
    private fun generateCenterCropMatrix(
        area: Area, width: Int, height: Int,
        extraSize: Float,
    ): Matrix {
        val rectF = area.areaRect

        val matrix = Matrix()

        // Calculate translation to center the drawable within the area.
        val offsetX = rectF.centerX() - width / 2
        val offsetY = rectF.centerY() - height / 2
        matrix.postTranslate(offsetX, offsetY)

        // Determine the scale factor to "crop" the drawable so that it fills the area completely.
        val scale = if (width * rectF.height() > rectF.width() * height) {
            (rectF.height() + extraSize) / height
        } else {
            (rectF.width() + extraSize) / width
        }
        matrix.postScale(scale, scale, rectF.centerX(), rectF.centerY())

        return matrix
    }
}

