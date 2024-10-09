package com.hypersoft.pzlayout.slant

import android.graphics.PointF
import com.hypersoft.pzlayout.interfaces.Line
import com.hypersoft.pzlayout.slant.SlantUtils.calculateSlope
import com.hypersoft.pzlayout.slant.SlantUtils.contains
import com.hypersoft.pzlayout.slant.SlantUtils.intersectionOfLines
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

/**
 * Represents a slanted line defined by its starting and ending points.
 * Implements the Line interface to provide methods for line manipulation
 * and calculations, including length, direction, and movement.
 */
class SlantLine : Line {
    var start: CrossoverPointF? = null // Starting point of the line
    var end: CrossoverPointF? = null // Ending point of the line

    private val previousStart = PointF() // Temporary storage for the previous start point
    private val previousEnd = PointF() // Temporary storage for the previous end point

    // The direction of the line (horizontal or vertical)
    val direction: Line.Direction

    var attachLineStart: SlantLine? = null // Line attached at the start
    var attachLineEnd: SlantLine? = null // Line attached at the end

    @JvmField
    var upperLine: Line? = null // Reference to the upper line

    @JvmField
    var lowerLine: Line? = null // Reference to the lower line

    // Constructor to initialize a SlantLine with a specified direction
    constructor(direction: Line.Direction) {
        this.direction = direction
    }

    // Constructor to initialize a SlantLine with start and end points and a specified direction
    constructor(start: CrossoverPointF?, end: CrossoverPointF?, direction: Line.Direction) {
        this.start = start
        this.end = end
        this.direction = direction
    }

    // Calculate the length of the line
    override fun length(): Float {
        return sqrt((end!!.x - start!!.x).toDouble().pow(2.0) + (end!!.y - start!!.y).toDouble().pow(2.0)).toFloat()
    }

    // Get the starting point of the line
    override fun startPoint(): PointF {
        return start!!
    }

    // Get the ending point of the line
    override fun endPoint(): PointF {
        return end!!
    }

    // Get the lower line reference
    override fun lowerLine(): Line {
        return lowerLine!!
    }

    // Get the upper line reference
    override fun upperLine(): Line {
        return upperLine!!
    }

    // Get the line attached at the start
    override fun attachStartLine(): Line {
        return attachLineStart!!
    }

    // Get the line attached at the end
    override fun attachEndLine(): Line {
        return attachLineEnd!!
    }

    // Set the reference for the lower line
    override fun setLowerLine(lowerLine: Line) {
        this.lowerLine = lowerLine
    }

    // Set the reference for the upper line
    override fun setUpperLine(upperLine: Line) {
        this.upperLine = upperLine
    }

    // Get the direction of the line
    override fun direction(): Line.Direction {
        return direction
    }

    // Calculate the slope of the line
    override fun slope(): Float {
        return calculateSlope(this)
    }

    // Check if a point (x, y) is within a specified distance (extra) from the line
    override fun contains(x: Float, y: Float, extra: Float): Boolean {
        return contains(this, x, y, extra)
    }

    // Move the line by a specified offset, checking bounds against attached lines
    override fun move(offset: Float, extra: Float): Boolean {
        if (direction == Line.Direction.HORIZONTAL) {
            // Check bounds for horizontal movement
            if (previousStart.y + offset < lowerLine!!.maxY() + extra || previousStart.y + offset > upperLine!!.minY() - extra ||
                previousEnd.y + offset < lowerLine!!.maxY() + extra || previousEnd.y + offset > upperLine!!.minY() - extra
            ) {
                return false // Movement out of bounds
            }

            start!!.y = previousStart.y + offset
            end!!.y = previousEnd.y + offset
        } else {
            // Check bounds for vertical movement
            if (previousStart.x + offset < lowerLine!!.maxX() + extra || previousStart.x + offset > upperLine!!.minX() - extra ||
                previousEnd.x + offset < lowerLine!!.maxX() + extra || previousEnd.x + offset > upperLine!!.minX() - extra
            ) {
                return false // Movement out of bounds
            }

            start!!.x = previousStart.x + offset
            end!!.x = previousEnd.x + offset
        }

        return true // Movement successful
    }

    // Prepare for movement by saving the current start and end points
    override fun prepareMove() {
        previousStart.set(start!!)
        previousEnd.set(end!!)
    }

    // Update the line based on its current position and attached lines
    override fun update(layoutWidth: Float, layoutHeight: Float) {
        intersectionOfLines(start!!, this, attachLineStart!!)
        intersectionOfLines(end!!, this, attachLineEnd!!)
    }

    // Get the minimum X value of the line
    override fun minX(): Float {
        return min(start!!.x.toDouble(), end!!.x.toDouble()).toFloat()
    }

    // Get the maximum X value of the line
    override fun maxX(): Float {
        return max(start!!.x.toDouble(), end!!.x.toDouble()).toFloat()
    }

    // Get the minimum Y value of the line
    override fun minY(): Float {
        return min(start!!.y.toDouble(), end!!.y.toDouble()).toFloat()
    }

    // Get the maximum Y value of the line
    override fun maxY(): Float {
        return max(start!!.y.toDouble(), end!!.y.toDouble()).toFloat()
    }

    // Offset the line by a specified amount in both x and y directions
    override fun offset(x: Float, y: Float) {
        start!!.offset(x, y)
        end!!.offset(x, y)
    }

    // Provide a string representation of the line
    override fun toString(): String {
        return "start --> " + start.toString() + ", end --> " + end.toString()
    }
}
