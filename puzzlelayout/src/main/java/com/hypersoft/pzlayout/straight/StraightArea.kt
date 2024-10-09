package com.hypersoft.pzlayout.straight

import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import com.hypersoft.pzlayout.interfaces.Area
import com.hypersoft.pzlayout.interfaces.Line
import java.util.Arrays

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

// StraightArea represents a rectangular area that can be defined by four straight lines.
// It provides methods to calculate the boundaries, center, and whether points or lines are within the area.
class StraightArea : Area {

    // Four straight lines that define the boundaries of this area.
    @JvmField
    var lineLeft: StraightLine? = null

    @JvmField
    var lineTop: StraightLine? = null

    @JvmField
    var lineRight: StraightLine? = null

    @JvmField
    var lineBottom: StraightLine? = null

    // Returns the path of the area, which is a rounded rectangle.
    override val areaPath: Path
        get() {
            val mAreaPath = Path()
            // Creates a path of the area with rounded corners based on 'radian'.
            mAreaPath.addRoundRect(areaRect, radian, radian, Path.Direction.CCW)
            return mAreaPath
        }

    // Returns the rectangular boundary of the area.
    override val areaRect: RectF
        get() {
            val mAreaRect = RectF()
            // Defines the rectangle by its left, top, right, and bottom positions.
            mAreaRect[left(), top(), right()] = bottom()
            return mAreaRect
        }

    // Stores two points that will be used for handle bars, i.e., the adjustable edges of the area.
    private val handleBarPoints = Array(2) { PointF() }

    // Padding values for the left, top, right, and bottom edges of the area.
    override var paddingLeft: Float = 0f
        private set
    override var paddingTop: Float = 0f
        private set
    override var paddingRight: Float = 0f
        private set
    override var paddingBottom: Float = 0f
        private set

    // The radian value that controls the roundness of the corners.
    private var radian = 0f

    // Default constructor that initializes the handle bar points.
    constructor() {
        handleBarPoints[0] = PointF()
        handleBarPoints[1] = PointF()
    }

    // Constructor that copies the properties from another StraightArea instance.
    constructor(src: StraightArea) {
        this.lineLeft = src.lineLeft
        this.lineTop = src.lineTop
        this.lineRight = src.lineRight
        this.lineBottom = src.lineBottom

        handleBarPoints[0] = PointF()
        handleBarPoints[1] = PointF()
    }

    // Returns the left boundary of the area considering the padding.
    override fun left(): Float {
        return lineLeft?.minX()?.plus(paddingLeft) ?: 0f
    }

    // Returns the top boundary of the area considering the padding.
    override fun top(): Float {
        return lineTop?.minY()?.plus(paddingLeft) ?: 0f
    }

    // Returns the right boundary of the area considering the padding.
    override fun right(): Float {
        return lineRight?.maxX()?.minus(paddingRight) ?: 0f
    }

    // Returns the bottom boundary of the area considering the padding.
    override fun bottom(): Float {
        return lineBottom?.maxY()?.minus(paddingBottom) ?: 0f
    }

    // Calculates and returns the horizontal center point of the area.
    override fun centerX(): Float {
        return (left() + right()) / 2
    }

    // Calculates and returns the vertical center point of the area.
    override fun centerY(): Float {
        return (top() + bottom()) / 2
    }

    // Calculates and returns the width of the area.
    override fun width(): Float {
        return right() - left()
    }

    // Calculates and returns the height of the area.
    override fun height(): Float {
        return bottom() - top()
    }

    // Returns the center point of the area as a PointF object.
    override val centerPoint: PointF
        get() = PointF(centerX(), centerY())

    // Checks if a given point is within the boundaries of the area.
    override fun contains(point: PointF?): Boolean {
        return contains(point!!.x, point.y)
    }

    // Checks if the given x and y coordinates are inside the area.
    override fun contains(x: Float, y: Float): Boolean {
        return areaRect.contains(x, y)
    }

    // Checks if a given line is one of the area’s boundary lines.
    override fun contains(line: Line?): Boolean {
        return lineLeft === line || lineTop === line || lineRight === line || lineBottom === line
    }

    // Returns a list of all boundary lines (left, top, right, bottom).
    override val lines: List<Line>
        get() = Arrays.asList(lineLeft as Line, lineTop, lineRight, lineBottom)

    // Returns the handle bar points for a given boundary line.
    // These points are used for resizing the area.
    override fun getHandleBarPoints(line: Line): Array<PointF> {
        if (line === lineLeft) {
            handleBarPoints[0].x = left()
            handleBarPoints[0].y = top() + height() / 4
            handleBarPoints[1].x = left()
            handleBarPoints[1].y = top() + height() / 4 * 3
        } else if (line === lineTop) {
            handleBarPoints[0].x = left() + width() / 4
            handleBarPoints[0].y = top()
            handleBarPoints[1].x = left() + width() / 4 * 3
            handleBarPoints[1].y = top()
        } else if (line === lineRight) {
            handleBarPoints[0].x = right()
            handleBarPoints[0].y = top() + height() / 4
            handleBarPoints[1].x = right()
            handleBarPoints[1].y = top() + height() / 4 * 3
        } else if (line === lineBottom) {
            handleBarPoints[0].x = left() + width() / 4
            handleBarPoints[0].y = bottom()
            handleBarPoints[1].x = left() + width() / 4 * 3
            handleBarPoints[1].y = bottom()
        }
        return handleBarPoints
    }

    // Returns the corner roundness of the area.
    override fun radian(): Float {
        return radian
    }

    // Sets the corner roundness of the area.
    override fun setRadian(radian: Float) {
        this.radian = radian
    }

    // Sets equal padding for all sides of the area.
    override fun setPadding(padding: Float) {
        setPadding(padding, padding, padding, padding)
    }

    // Sets custom padding values for each side of the area.
    override fun setPadding(paddingLeft: Float, paddingTop: Float, paddingRight: Float, paddingBottom: Float) {
        this.paddingLeft = paddingLeft
        this.paddingTop = paddingTop
        this.paddingRight = paddingRight
        this.paddingBottom = paddingBottom
    }

    // Comparator for sorting StraightArea instances by their top and left positions.
    internal class AreaComparator : Comparator<StraightArea> {
        // Compares two areas based on their top and left values for sorting.
        override fun compare(lhs: StraightArea, rhs: StraightArea): Int {
            return if (lhs.top() < rhs.top()) {
                -1
            } else if (lhs.top() == rhs.top()) {
                if (lhs.left() < rhs.left()) {
                    -1
                } else if (lhs.left() == rhs.left()) {
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
