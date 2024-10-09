package com.hypersoft.pzlayout.slant

import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import com.hypersoft.pzlayout.interfaces.Area
import com.hypersoft.pzlayout.interfaces.Line
import com.hypersoft.pzlayout.slant.SlantUtils.contains
import com.hypersoft.pzlayout.slant.SlantUtils.distance
import com.hypersoft.pzlayout.slant.SlantUtils.getPoint
import com.hypersoft.pzlayout.slant.SlantUtils.intersectionOfLines
import java.util.Arrays
import kotlin.math.max
import kotlin.math.min

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

/**
 * Represents a slanted area defined by four lines (left, top, right, bottom)
 * and its corner points. The area can have padding and can be adjusted with
 * a slant angle (radian). It implements the Area interface to provide
 * methods for area calculations and manipulations.
 */
class SlantArea internal constructor() : Area {
    // The slant lines defining the edges of the area
    @JvmField
    var lineLeft: SlantLine? = null

    @JvmField
    var lineTop: SlantLine? = null

    @JvmField
    var lineRight: SlantLine? = null

    @JvmField
    var lineBottom: SlantLine? = null

    // Corner points of the area
    var leftTop: CrossoverPointF
    var leftBottom: CrossoverPointF
    var rightTop: CrossoverPointF
    var rightBottom: CrossoverPointF

    private val tempPoint: PointF // Temporary point used for calculations

    // Padding for the area
    override var paddingLeft: Float = 0f
        private set
    override var paddingTop: Float = 0f
        private set
    override var paddingRight: Float = 0f
        private set
    override var paddingBottom: Float = 0f
        private set

    private var radian = 0f // Slant angle in radians

    // Path that represents the area shape
    override val areaPath: Path
        get() {
            val _areaPath = Path()
            if (radian > 0) {
                // Calculate path points when there is a slant
                var tempRatio = radian / distance(leftTop, leftBottom)
                getPoint(tempPoint, leftTop, leftBottom, Line.Direction.VERTICAL, tempRatio)
                tempPoint.offset(paddingLeft, paddingTop)
                _areaPath.moveTo(tempPoint.x, tempPoint.y)

                // Create the path with quadratic curves based on the slant
                tempRatio = radian / distance(leftTop, rightTop)
                getPoint(tempPoint, leftTop, rightTop, Line.Direction.HORIZONTAL, tempRatio)
                tempPoint.offset(paddingLeft, paddingTop)
                _areaPath.quadTo(leftTop.x + paddingLeft, leftTop.y + paddingTop, tempPoint.x, tempPoint.y)

                tempRatio = 1 - tempRatio
                getPoint(tempPoint, leftTop, rightTop, Line.Direction.HORIZONTAL, tempRatio)
                tempPoint.offset(-paddingRight, paddingTop)
                _areaPath.lineTo(tempPoint.x, tempPoint.y)

                tempRatio = radian / distance(rightTop, rightBottom)
                getPoint(tempPoint, rightTop, rightBottom, Line.Direction.VERTICAL, tempRatio)
                tempPoint.offset(-paddingRight, paddingTop)
                _areaPath.quadTo(rightTop.x - paddingLeft, rightTop.y + paddingTop, tempPoint.x, tempPoint.y)

                tempRatio = 1 - tempRatio
                getPoint(tempPoint, rightTop, rightBottom, Line.Direction.VERTICAL, tempRatio)
                tempPoint.offset(-paddingRight, -paddingBottom)
                _areaPath.lineTo(tempPoint.x, tempPoint.y)

                tempRatio = 1 - radian / distance(leftBottom, rightBottom)
                getPoint(tempPoint, leftBottom, rightBottom, Line.Direction.HORIZONTAL, tempRatio)
                tempPoint.offset(-paddingRight, -paddingBottom)
                _areaPath.quadTo(rightBottom.x - paddingRight, rightBottom.y - paddingTop, tempPoint.x, tempPoint.y)

                tempRatio = 1 - tempRatio
                getPoint(tempPoint, leftBottom, rightBottom, Line.Direction.HORIZONTAL, tempRatio)
                tempPoint.offset(paddingLeft, -paddingBottom)
                _areaPath.lineTo(tempPoint.x, tempPoint.y)

                tempRatio = 1 - radian / distance(leftTop, leftBottom)
                getPoint(tempPoint, leftTop, leftBottom, Line.Direction.VERTICAL, tempRatio)
                tempPoint.offset(paddingLeft, -paddingBottom)
                _areaPath.quadTo(leftBottom.x + paddingLeft, leftBottom.y - paddingBottom, tempPoint.x, tempPoint.y)

                tempRatio = 1 - tempRatio
                getPoint(tempPoint, leftTop, leftBottom, Line.Direction.VERTICAL, tempRatio)
                tempPoint.offset(paddingLeft, paddingTop)
                _areaPath.lineTo(tempPoint.x, tempPoint.y)
            } else {
                // Create a rectangular area path when there is no slant
                _areaPath.moveTo(leftTop.x + paddingLeft, leftTop.y + paddingTop)
                _areaPath.lineTo(rightTop.x - paddingRight, rightTop.y + paddingTop)
                _areaPath.lineTo(rightBottom.x - paddingRight, rightBottom.y - paddingBottom)
                _areaPath.lineTo(leftBottom.x + paddingLeft, leftBottom.y - paddingBottom)
                _areaPath.lineTo(leftTop.x + paddingLeft, leftTop.y + paddingTop)
            }
            return _areaPath
        }

    // Rectangle representing the area bounds
    override val areaRect: RectF
        get() {
            val _areaRect = RectF()
            _areaRect[left(), top(), right()] = bottom()
            return _areaRect
        }

    private val handleBarPoints = Array(2) { PointF() } // Handle points for manipulation

    init {
        // Initialize corner points and temporary point
        handleBarPoints[0] = PointF()
        handleBarPoints[1] = PointF()

        leftTop = CrossoverPointF()
        leftBottom = CrossoverPointF()
        rightTop = CrossoverPointF()
        rightBottom = CrossoverPointF()

        tempPoint = PointF()
    }

    // Copy constructor for creating a new SlantArea from an existing one
    internal constructor(src: SlantArea) : this() {
        this.lineLeft = src.lineLeft
        this.lineTop = src.lineTop
        this.lineRight = src.lineRight
        this.lineBottom = src.lineBottom

        this.leftTop = src.leftTop
        this.leftBottom = src.leftBottom
        this.rightTop = src.rightTop
        this.rightBottom = src.rightBottom

        updateCornerPoints() // Update corner points after copying
    }

    // Calculate the left boundary of the area
    override fun left(): Float {
        return (min(leftTop.x.toDouble(), leftBottom.x.toDouble()) + paddingLeft).toFloat()
    }

    // Calculate the top boundary of the area
    override fun top(): Float {
        return (min(leftTop.y.toDouble(), rightTop.y.toDouble()) + paddingTop).toFloat()
    }

    // Calculate the right boundary of the area
    override fun right(): Float {
        return (max(rightTop.x.toDouble(), rightBottom.x.toDouble()) - paddingRight).toFloat()
    }

    // Calculate the bottom boundary of the area
    override fun bottom(): Float {
        return (max(leftBottom.y.toDouble(), rightBottom.y.toDouble()) - paddingBottom).toFloat()
    }

    // Calculate the center X position of the area
    override fun centerX(): Float {
        return (left() + right()) / 2
    }

    // Calculate the center Y position of the area
    override fun centerY(): Float {
        return (top() + bottom()) / 2
    }

    // Calculate the width of the area
    override fun width(): Float {
        return right() - left()
    }

    // Calculate the height of the area
    override fun height(): Float {
        return bottom() - top()
    }

    // Get the center point of the area
    override val centerPoint: PointF
        get() = PointF(centerX(), centerY())

    // Check if a point is within the area
    override fun contains(x: Float, y: Float): Boolean {
        return contains(this, x, y)
    }

    // Check if a line is part of this area
    override fun contains(line: Line?): Boolean {
        return lineLeft == line || lineTop == line || lineRight == line || lineBottom == line
    }

    // Check if a point is part of this area
    override fun contains(point: PointF?): Boolean {
        return contains(point!!.x, point.y)
    }

    // Get the list of lines that make up the area
    override val lines: List<Line>
        get() = Arrays.asList(lineLeft as Line, lineTop, lineRight, lineBottom)

    // Get handle points for a specified line
    override fun getHandleBarPoints(line: Line): Array<PointF> {
        if (line === lineLeft) {
            getPoint(handleBarPoints[0], leftTop, leftBottom, line.direction(), 0.25f)
            getPoint(handleBarPoints[1], leftTop, leftBottom, line.direction(), 0.75f)
            handleBarPoints[0].offset(paddingLeft, 0f)
            handleBarPoints[1].offset(paddingLeft, 0f)
        } else if (line === lineTop) {
            getPoint(handleBarPoints[0], leftTop, rightTop, line.direction(), 0.25f)
            getPoint(handleBarPoints[1], leftTop, rightTop, line.direction(), 0.75f)
            handleBarPoints[0].offset(0f, paddingTop)
            handleBarPoints[1].offset(0f, paddingTop)
        } else if (line === lineRight) {
            getPoint(handleBarPoints[0], rightTop, rightBottom, line.direction(), 0.25f)
            getPoint(handleBarPoints[1], rightTop, rightBottom, line.direction(), 0.75f)
            handleBarPoints[0].offset(-paddingRight, 0f)
            handleBarPoints[1].offset(-paddingRight, 0f)
        } else if (line === lineBottom) {
            getPoint(handleBarPoints[0], leftBottom, rightBottom, line.direction(), 0.25f)
            getPoint(handleBarPoints[1], leftBottom, rightBottom, line.direction(), 0.75f)
            handleBarPoints[0].offset(0f, -paddingBottom)
            handleBarPoints[1].offset(0f, -paddingBottom)
        }
        return handleBarPoints
    }

    // Get the current slant angle in radians
    override fun radian(): Float {
        return radian
    }

    // Set the slant angle in radians
    override fun setRadian(radian: Float) {
        this.radian = radian
    }

    // Set uniform padding for the area
    override fun setPadding(padding: Float) {
        setPadding(padding, padding, padding, padding)
    }

    // Set individual padding for the area
    override fun setPadding(paddingLeft: Float, paddingTop: Float, paddingRight: Float, paddingBottom: Float) {
        this.paddingLeft = paddingLeft
        this.paddingTop = paddingTop
        this.paddingRight = paddingRight
        this.paddingBottom = paddingBottom
    }

    // Update the corner points based on the current lines
    fun updateCornerPoints() {
        intersectionOfLines(leftTop, lineLeft!!, lineTop!!)
        intersectionOfLines(leftBottom, lineLeft!!, lineBottom!!)
        intersectionOfLines(rightTop, lineRight!!, lineTop!!)
        intersectionOfLines(rightBottom, lineRight!!, lineBottom!!)
    }

    /**
     * Comparator for comparing two SlantArea objects based on their
     * top-left corner position. Used for sorting.
     */
    internal class AreaComparator : Comparator<SlantArea> {
        override fun compare(one: SlantArea, two: SlantArea): Int {
            return if (one.leftTop.y < two.leftTop.y) {
                -1
            } else if (one.leftTop.y == two.leftTop.y) {
                if (one.leftTop.x < two.leftTop.x) {
                    -1
                } else if (one.leftTop.x == two.leftTop.x) {
                    0
                } else {
                    1
                }
            } else {
                1
            }
        }
    }
}
