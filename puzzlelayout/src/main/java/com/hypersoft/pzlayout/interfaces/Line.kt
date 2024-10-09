package com.hypersoft.pzlayout.interfaces

import android.graphics.PointF

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

/**
 * Interface representing a line with various properties and methods
 * for managing its dimensions, position, and interactions.
 */
interface Line {

    /**
     * Enumeration representing the possible directions of a line.
     */
    enum class Direction {
        HORIZONTAL, // Indicates a horizontal line
        VERTICAL    // Indicates a vertical line
    }

    /**
     * Returns the length of the line.
     *
     * @return The length of the line as a Float.
     */
    fun length(): Float

    /**
     * Returns the starting point of the line as a PointF object.
     *
     * @return The starting point of the line.
     */
    fun startPoint(): PointF

    /**
     * Returns the ending point of the line as a PointF object.
     *
     * @return The ending point of the line.
     */
    fun endPoint(): PointF

    /**
     * Returns the line that is lower than the current line.
     *
     * @return The lower line.
     */
    fun lowerLine(): Line

    /**
     * Returns the line that is upper than the current line.
     *
     * @return The upper line.
     */
    fun upperLine(): Line

    /**
     * Returns the line that is attached to the start of the current line.
     *
     * @return The attached start line.
     */
    fun attachStartLine(): Line

    /**
     * Returns the line that is attached to the end of the current line.
     *
     * @return The attached end line.
     */
    fun attachEndLine(): Line

    /**
     * Sets the specified line as the lower line.
     *
     * @param lowerLine The line to set as the lower line.
     */
    fun setLowerLine(lowerLine: Line)

    /**
     * Sets the specified line as the upper line.
     *
     * @param upperLine The line to set as the upper line.
     */
    fun setUpperLine(upperLine: Line)

    /**
     * Returns the direction of the line (HORIZONTAL or VERTICAL).
     *
     * @return The direction of the line.
     */
    fun direction(): Direction

    /**
     * Returns the slope of the line.
     *
     * @return The slope of the line as a Float.
     */
    fun slope(): Float

    /**
     * Checks if the specified coordinates (x, y) are within the line,
     * allowing for a specified extra margin.
     *
     * @param x The X coordinate to check.
     * @param y The Y coordinate to check.
     * @param extra Additional margin for the check.
     * @return True if the point is within the line, false otherwise.
     */
    fun contains(x: Float, y: Float, extra: Float): Boolean

    /**
     * Prepares the line for moving.
     */
    fun prepareMove()

    /**
     * Moves the line by a specified offset and an extra margin.
     *
     * @param offset The distance to move the line.
     * @param extra Additional margin for the move.
     * @return True if the move was successful, false otherwise.
     */
    fun move(offset: Float, extra: Float): Boolean

    /**
     * Updates the line's properties based on the layout's dimensions.
     *
     * @param layoutWidth The width of the layout.
     * @param layoutHeight The height of the layout.
     */
    fun update(layoutWidth: Float, layoutHeight: Float)

    /**
     * Returns the minimum X coordinate of the line.
     *
     * @return The minimum X coordinate as a Float.
     */
    fun minX(): Float

    /**
     * Returns the maximum X coordinate of the line.
     *
     * @return The maximum X coordinate as a Float.
     */
    fun maxX(): Float

    /**
     * Returns the minimum Y coordinate of the line.
     *
     * @return The minimum Y coordinate as a Float.
     */
    fun minY(): Float

    /**
     * Returns the maximum Y coordinate of the line.
     *
     * @return The maximum Y coordinate as a Float.
     */
    fun maxY(): Float

    /**
     * Offsets the line by the specified x and y values.
     *
     * @param x The offset value for the X coordinate.
     * @param y The offset value for the Y coordinate.
     */
    fun offset(x: Float, y: Float)
}
