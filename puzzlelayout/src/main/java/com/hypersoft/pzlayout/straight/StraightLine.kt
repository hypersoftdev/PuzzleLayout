package com.hypersoft.pzlayout.straight

import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import com.hypersoft.pzlayout.interfaces.Line
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

// The StraightLine class represents a line that can either be horizontal or vertical.
// It defines behavior for moving and interacting with the line within a layout.
class StraightLine(private val start: PointF, private val end: PointF) : Line {

    // Previous start and end points of the line, used for calculating movement.
    private val previousStart = PointF()
    private val previousEnd = PointF()

    // Direction of the line (either HORIZONTAL or VERTICAL).
    var direction: Line.Direction = Line.Direction.HORIZONTAL

    // Lines attached to the start and end points of this line.
    @JvmField
    var attachLineStart: StraightLine? = null

    @JvmField
    var attachLineEnd: StraightLine? = null

    // Lines that define the upper and lower boundaries for movement.
    private var upperLine: Line? = null
    private var lowerLine: Line? = null

    // Defines the bounds for detecting whether a point is within the line.
    private val bounds = RectF()

    // Initializes the direction based on the start and end points.
    init {
        if (start.x == end.x) {
            direction = Line.Direction.VERTICAL
        } else if (start.y == end.y) {
            direction = Line.Direction.HORIZONTAL
        } else {
            Log.d("StraightLine", "StraightLine: current only supports two directions (horizontal or vertical)")
        }
    }

    // Returns the length of the line.
    override fun length(): Float {
        return sqrt((end.x - start.x).toDouble().pow(2.0) + (end.y - start.y).toDouble().pow(2.0)).toFloat()
    }

    // Returns the start point of the line.
    override fun startPoint(): PointF {
        return start
    }

    // Returns the end point of the line.
    override fun endPoint(): PointF {
        return end
    }

    // Returns the lower boundary line.
    override fun lowerLine(): Line {
        return lowerLine!!
    }

    // Returns the upper boundary line.
    override fun upperLine(): Line {
        return upperLine!!
    }

    // Returns the line attached to the start of this line.
    override fun attachStartLine(): Line {
        return attachLineStart!!
    }

    // Returns the line attached to the end of this line.
    override fun attachEndLine(): Line {
        return attachLineEnd!!
    }

    // Sets the lower boundary line for movement.
    override fun setLowerLine(lowerLine: Line) {
        this.lowerLine = lowerLine
    }

    // Sets the upper boundary line for movement.
    override fun setUpperLine(upperLine: Line) {
        this.upperLine = upperLine
    }

    // Sets the line attached to the start of this line.
    fun setAttachLineStart(attachLineStart: StraightLine?) {
        this.attachLineStart = attachLineStart
    }

    // Sets the line attached to the end of this line.
    fun setAttachLineEnd(attachLineEnd: StraightLine?) {
        this.attachLineEnd = attachLineEnd
    }

    // Returns the direction of the line (HORIZONTAL or VERTICAL).
    override fun direction(): Line.Direction {
        return direction
    }

    // Returns the slope of the line. Horizontal lines have a slope of 0, vertical lines have a very high slope.
    override fun slope(): Float {
        return if (direction == Line.Direction.HORIZONTAL) 0f else Float.MAX_VALUE
    }

    // Checks whether a point (x, y) with some extra width or height lies within the line.
    override fun contains(x: Float, y: Float, extra: Float): Boolean {
        if (direction == Line.Direction.HORIZONTAL) {
            bounds.left = start.x
            bounds.right = end.x
            bounds.top = start.y - extra / 2
            bounds.bottom = start.y + extra / 2
        } else if (direction == Line.Direction.VERTICAL) {
            bounds.top = start.y
            bounds.bottom = end.y
            bounds.left = start.x - extra / 2
            bounds.right = start.x + extra / 2
        }

        return bounds.contains(x, y)
    }

    // Saves the current position of the line before it is moved.
    override fun prepareMove() {
        previousStart.set(start)
        previousEnd.set(end)
    }

    // Moves the line by an offset, but ensures that it does not go beyond its upper or lower boundaries.
    override fun move(offset: Float, extra: Float): Boolean {
        if (direction == Line.Direction.HORIZONTAL) {
            // Checks if the line can be moved within its upper and lower boundaries.
            if (previousStart.y + offset < lowerLine!!.maxY() + extra || previousStart.y + offset > upperLine!!.minY() - extra || previousEnd.y + offset < lowerLine!!.maxY() + extra || previousEnd.y + offset > upperLine!!.minY() - extra) {
                return false
            }

            start.y = previousStart.y + offset
            end.y = previousEnd.y + offset
        } else {
            // Checks if the line can be moved within its left and right boundaries.
            if (previousStart.x + offset < lowerLine!!.maxX() + extra || previousStart.x + offset > upperLine!!.minX() - extra || previousEnd.x + offset < lowerLine!!.maxX() + extra || previousEnd.x + offset > upperLine!!.minX() - extra) {
                return false
            }

            start.x = previousStart.x + offset
            end.x = previousEnd.x + offset
        }

        return true
    }

    // Updates the line’s position based on its attached start and end lines.
    override fun update(layoutWidth: Float, layoutHeight: Float) {
        if (direction == Line.Direction.HORIZONTAL) {
            if (attachLineStart != null) {
                start.x = attachLineStart!!.position
            }
            if (attachLineEnd != null) {
                end.x = attachLineEnd!!.position
            }
        } else if (direction == Line.Direction.VERTICAL) {
            if (attachLineStart != null) {
                start.y = attachLineStart!!.position
            }
            if (attachLineEnd != null) {
                end.y = attachLineEnd!!.position
            }
        }
    }

    // Returns the current position of the line. For horizontal lines, this is the y-coordinate, and for vertical lines, this is the x-coordinate.
    val position: Float
        get() = if (direction == Line.Direction.HORIZONTAL) {
            start.y
        } else {
            start.x
        }

    // Returns the minimum x-coordinate of the line.
    override fun minX(): Float {
        return min(start.x.toDouble(), end.x.toDouble()).toFloat()
    }

    // Returns the maximum x-coordinate of the line.
    override fun maxX(): Float {
        return max(start.x.toDouble(), end.x.toDouble()).toFloat()
    }

    // Returns the minimum y-coordinate of the line.
    override fun minY(): Float {
        return min(start.y.toDouble(), end.y.toDouble()).toFloat()
    }

    // Returns the maximum y-coordinate of the line.
    override fun maxY(): Float {
        return max(start.y.toDouble(), end.y.toDouble()).toFloat()
    }

    // Offsets the line by a given x and y value.
    override fun offset(x: Float, y: Float) {
        start.offset(x, y)
        end.offset(x, y)
    }

    // Returns a string representation of the line, showing the start and end points.
    override fun toString(): String {
        return "start --> $start, end --> $end"
    }
}
