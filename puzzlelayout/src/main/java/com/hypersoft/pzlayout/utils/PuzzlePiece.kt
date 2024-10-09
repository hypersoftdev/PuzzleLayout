package com.hypersoft.pzlayout.utils

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.hypersoft.pzlayout.interfaces.Area
import com.hypersoft.pzlayout.interfaces.Line
import com.hypersoft.pzlayout.utils.MatrixUtils.getMatrixAngle
import com.hypersoft.pzlayout.utils.MatrixUtils.getMatrixScale
import com.hypersoft.pzlayout.utils.MatrixUtils.getMinMatrixScale

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */
class PuzzlePiece internal constructor(
    @JvmField var drawable: Drawable, // The image or shape drawn within this puzzle piece
    @JvmField var area: Area, // Defines the boundaries or area of the puzzle piece
    @JvmField val matrix: Matrix, // Transformation matrix for scaling, translation, and rotation
) {

    private val previousMatrix = Matrix() // Stores the matrix state before transformations
    private var drawableBounds: Rect // The bounds of the drawable object
    private var drawablePoints: FloatArray // Points that define the corners of the drawable
    private val mappedDrawablePoints: FloatArray // Transformed points after applying matrix

    private var previousMoveX = 0f // Tracks the previous X position during movement
    private var previousMoveY = 0f // Tracks the previous Y position during movement

    private val mappedBounds: RectF // Stores the transformed bounds of the drawable
    private val centerPoint: PointF // The center point of the drawable's area
    private val mappedCenterPoint: PointF // The center point after matrix transformation

    private val animator: ValueAnimator // Animation for smooth movement or transformations
    private var duration = 300 // Default animation duration in milliseconds
    private val tempMatrix: Matrix // Temporary matrix for calculations during transformations

    @JvmField
    var path: String = "" // Stores the path or identifier for this puzzle piece

    init {
        // Initialize the drawable bounds and points based on the drawable's size
        this.drawableBounds = Rect(0, 0, width, height)
        this.drawablePoints = floatArrayOf(
            0f, 0f, width.toFloat(), 0f, width.toFloat(), height.toFloat(), 0f, height.toFloat()
        )
        this.mappedDrawablePoints = FloatArray(8) // Allocate space for 4 corner points

        this.mappedBounds = RectF() // Holds the transformed bounds
        this.centerPoint = PointF(area.centerX(), area.centerY()) // Set the initial center point
        this.mappedCenterPoint = PointF() // Holds the transformed center point

        this.animator = ValueAnimator.ofFloat(0f, 1f) // Initialize the animator for smooth transitions
        animator.interpolator = DecelerateInterpolator() // Slow down animations at the end

        this.tempMatrix = Matrix() // Temp matrix for intermediate calculations
    }

    // Draw the puzzle piece on the canvas
    fun draw(canvas: Canvas, quickMode: Boolean) {
        draw(canvas, 255, true, quickMode)
    }

    // Overloaded function to allow setting alpha transparency while drawing
    fun draw(canvas: Canvas, alpha: Int, quickMode: Boolean) {
        draw(canvas, alpha, false, quickMode)
    }

    // Main function for drawing the drawable on the canvas
    private fun draw(canvas: Canvas, alpha: Int, needClip: Boolean, quickMode: Boolean) {
        if ((drawable is BitmapDrawable) && !quickMode) {
            // Save the canvas state to handle complex drawing operations
            val saved = canvas.saveLayer(null, null)

            val bitmap = (drawable as BitmapDrawable).bitmap
            val paint = (drawable as BitmapDrawable).paint

            // Set the paint color and alpha for the drawable
            paint.color = Color.WHITE
            paint.alpha = alpha
            canvas.clipPath(area.areaPath) // Clip the drawable to the puzzle piece's area
            canvas.drawBitmap(bitmap, matrix, paint) // Draw the bitmap with transformations
            paint.xfermode = null
            canvas.restoreToCount(saved) // Restore the previous canvas state
        } else {
            // Draw a non-bitmap drawable or in quick mode
            canvas.save()
            if (needClip) {
                canvas.clipPath(area.areaPath) // Clip drawable to the puzzle piece's area
            }
            canvas.concat(matrix) // Apply transformations (scale, rotate, translate)
            drawable.bounds = drawableBounds // Set drawable bounds
            drawable.alpha = alpha // Set transparency level
            drawable.draw(canvas) // Draw the drawable on the canvas
            canvas.restore()
        }
    }

    // Sets a new drawable and updates its bounds and points
    fun setDrawable(drawable: Drawable) {
        this.drawable = drawable
        this.drawableBounds = Rect(0, 0, width, height)
        this.drawablePoints = floatArrayOf(
            0f, 0f, width.toFloat(), 0f, width.toFloat(), height.toFloat(), 0f, height.toFloat()
        )
    }

    // Returns the current drawable
    fun getDrawable(): Drawable {
        return drawable
    }

    // Get the width of the drawable
    val width: Int
        get() = drawable.intrinsicWidth

    // Get the height of the drawable
    val height: Int
        get() = drawable.intrinsicHeight

    // Check if the (x, y) point lies within the puzzle piece area
    fun contains(x: Float, y: Float): Boolean {
        return area.contains(x, y)
    }

    // Check if a line intersects with the puzzle piece area
    fun contains(line: Line?): Boolean {
        return area.contains(line)
    }

    // Set the previous X coordinate for dragging or movement
    fun setPreviousMoveX(previousMoveX: Float) {
        this.previousMoveX = previousMoveX
    }

    // Set the previous Y coordinate for dragging or movement
    fun setPreviousMoveY(previousMoveY: Float) {
        this.previousMoveY = previousMoveY
    }

    // Get the current bounds of the drawable after applying matrix transformations
    private val currentDrawableBounds: RectF
        get() {
            matrix.mapRect(mappedBounds, RectF(drawableBounds))
            return mappedBounds
        }

    // Get the center point of the transformed drawable
    private val currentDrawableCenterPoint: PointF
        get() {
            currentDrawableBounds
            mappedCenterPoint.x = mappedBounds.centerX()
            mappedCenterPoint.y = mappedBounds.centerY()
            return mappedCenterPoint
        }

    // Get the center point of the puzzle piece's area
    val areaCenterPoint: PointF
        get() {
            centerPoint.x = area.centerX()
            centerPoint.y = area.centerY()
            return centerPoint
        }

    // Get the current scale factor of the matrix
    private val matrixScale: Float
        get() = getMatrixScale(matrix)

    // Get the current rotation angle of the matrix
    val matrixAngle: Float
        get() = getMatrixAngle(matrix)

    // Get the transformed corner points of the drawable
    val currentDrawablePoints: FloatArray
        get() {
            matrix.mapPoints(mappedDrawablePoints, drawablePoints)
            return mappedDrawablePoints
        }

    // Check if the drawable completely fills the area of the puzzle piece
    val isFilledArea: Boolean
        get() {
            val bounds = currentDrawableBounds
            return !(bounds.left > area.left() || bounds.top > area.top() || bounds.right < area.right() || bounds.bottom < area.bottom())
        }

    // Check if the drawable can fill the area at the current scale
    fun canFilledArea(): Boolean {
        val scale = getMatrixScale(matrix)
        val minScale = getMinMatrixScale(this)
        return scale >= minScale
    }

    // Record the current matrix state (useful for undoing transformations)
    fun record() {
        previousMatrix.set(matrix)
    }

    // Translate the drawable by a given offset (X, Y)
    fun translate(offsetX: Float, offsetY: Float) {
        matrix.set(previousMatrix)
        postTranslate(offsetX, offsetY)
    }

    // Scale the drawable from a midpoint
    fun zoom(scaleX: Float, scaleY: Float, midPoint: PointF) {
        matrix.set(previousMatrix)
        postScale(scaleX, scaleY, midPoint)
    }

    // Scale and translate the drawable simultaneously
    fun zoomAndTranslate(scaleX: Float, scaleY: Float, midPoint: PointF, offsetX: Float, offsetY: Float) {
        matrix.set(previousMatrix)
        postTranslate(offsetX, offsetY)
        postScale(scaleX, scaleY, midPoint)
    }

    // Set the matrix to a new transformation
    fun set(matrix: Matrix?) {
        this.matrix.set(matrix)
        moveToFillArea(null)
    }

    // Apply a translation transformation
    private fun postTranslate(x: Float, y: Float) {
        matrix.postTranslate(x, y)
    }

    // Apply a scaling transformation
    private fun postScale(scaleX: Float, scaleY: Float, midPoint: PointF) {
        matrix.postScale(scaleX, scaleY, midPoint.x, midPoint.y)
    }

    // Flip the drawable vertically
    fun postFlipVertically() {
        matrix.postScale(1f, -1f, area.centerX(), area.centerY())
    }

    // Flip the drawable horizontally
    fun postFlipHorizontally() {
        matrix.postScale(-1f, 1f, area.centerX(), area.centerY())
    }

    // Rotate the drawable by a certain degree
    fun postRotate(degree: Float) {
        matrix.postRotate(degree, area.centerX(), area.centerY())

        // Ensure the drawable does not become too small after rotation
        val minScale = getMinMatrixScale(this)
        if (matrixScale < minScale) {
            val midPoint = PointF()
            midPoint.set(currentDrawableCenterPoint)
        }
    }

    // Animate translation of the drawable smoothly over time
    private fun animateTranslate(view: View, translateX: Float, translateY: Float) {
        animator.end()
        animator.removeAllUpdateListeners()
        animator.addUpdateListener { animation ->
            val x = translateX * animation.animatedValue as Float
            val y = translateY * animation.animatedValue as Float

            translate(x, y)
            view.invalidate()
        }
        animator.duration = duration.toLong() // Set animation duration
        animator.start() // Start the animation
    }

    // Move the drawable to ensure it fits completely within the puzzle piece area
    fun moveToFillArea(view: View?) {
        Log.d("Debugging ", "moveToFillArea")
        if (isFilledArea) return
        record()

        val rectF = currentDrawableBounds
        var offsetX = 0f
        var offsetY = 0f

        if (rectF.left > area.left()) {
            offsetX = area.left() - rectF.left
        }

        if (rectF.top > area.top()) {
            offsetY = area.top() - rectF.top
        }

        if (rectF.right < area.right()) {
            offsetX = area.right() - rectF.right
        }

        if (rectF.bottom < area.bottom()) {
            offsetY = area.bottom() - rectF.bottom
        }

        if (view == null) {
            postTranslate(offsetX, offsetY)
        } else {
            animateTranslate(view, offsetX, offsetY)
        }
    }

    // Scale and move the drawable to fill the puzzle piece area, optionally with animation
    fun fillArea(view: View, quick: Boolean) {
        Log.d("Debugging ", "fillArea")
        if (isFilledArea) return
        record()

        val startScale = matrixScale
        val endScale = getMinMatrixScale(this)

        val midPoint = PointF()
        midPoint.set(currentDrawableCenterPoint)

        tempMatrix.set(matrix)
        tempMatrix.postScale(endScale / startScale, endScale / startScale, midPoint.x, midPoint.y)

        val rectF = RectF(drawableBounds)
        tempMatrix.mapRect(rectF)

        var offsetX = 0f
        var offsetY = 0f

        if (rectF.left > area.left()) {
            offsetX = area.left() - rectF.left
        }

        if (rectF.top > area.top()) {
            offsetY = area.top() - rectF.top
        }

        if (rectF.right < area.right()) {
            offsetX = area.right() - rectF.right
        }

        if (rectF.bottom < area.bottom()) {
            offsetY = area.bottom() - rectF.bottom
        }

        val translateX = offsetX
        val translateY = offsetY

        animator.end()
        animator.removeAllUpdateListeners()
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            val scale = (startScale + (endScale - startScale) * value) / startScale
            val x = translateX * value
            val y = translateY * value

            zoom(scale, scale, midPoint)
            postTranslate(x, y)
            view.invalidate()
        }

        if (quick) {
            animator.duration = 0
        } else {
            animator.duration = duration.toLong()
        }
        animator.start()
    }

    // Update the puzzle piece's transformation during a touch event
    fun updateWith(event: MotionEvent, line: Line) {
        Log.d("Debugging ", "updateWith")
        val offsetX = (event.x - previousMoveX) / 2
        val offsetY = (event.y - previousMoveY) / 2

        if (!canFilledArea()) {
            val area = area
            postScale(1.01f, 1.01f, area.centerPoint)
            record()
            previousMoveX = event.x
            previousMoveY = event.y
        }

        if (line.direction() == Line.Direction.HORIZONTAL) {
            translate(0f, offsetY)
        } else if (line.direction() == Line.Direction.VERTICAL) {
            translate(offsetX, 0f)
        }

        val rectF = currentDrawableBounds
        val area = area
        var moveY = 0f

        if (rectF.top > area.top()) {
            moveY = area.top() - rectF.top
        }

        if (rectF.bottom < area.bottom()) {
            moveY = area.bottom() - rectF.bottom
        }

        var moveX = 0f

        if (rectF.left > area.left()) {
            moveX = area.left() - rectF.left
        }

        if (rectF.right < area.right()) {
            moveX = area.right() - rectF.right
        }

        if (moveX != 0f || moveY != 0f) {
            previousMoveX = event.x
            previousMoveY = event.y
            record()
        }
    }

    // Check if the animation is currently running
    val isAnimateRunning: Boolean
        get() = animator.isRunning

    // Set the duration of animations
    fun setAnimateDuration(duration: Int) {
        this.duration = duration
    }
}
