package com.hypersoft.pzlayout.interfaces

import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

/**
 * Interface representing a geometric area with various properties and methods
 * for managing its dimensions, position, and interactions with points and lines.
 */
interface Area {

    /**
     * Returns the left coordinate of the area.
     */
    fun left(): Float

    /**
     * Returns the top coordinate of the area.
     */
    fun top(): Float

    /**
     * Returns the right coordinate of the area.
     */
    fun right(): Float

    /**
     * Returns the bottom coordinate of the area.
     */
    fun bottom(): Float

    /**
     * Returns the X coordinate of the center of the area.
     */
    fun centerX(): Float

    /**
     * Returns the Y coordinate of the center of the area.
     */
    fun centerY(): Float

    /**
     * Returns the width of the area.
     */
    fun width(): Float

    /**
     * Returns the height of the area.
     */
    fun height(): Float

    /**
     * Returns the center point of the area as a PointF object.
     */
    val centerPoint: PointF

    /**
     * Checks if the area contains a given point.
     *
     * @param point The point to check.
     * @return True if the area contains the point, false otherwise.
     */
    fun contains(point: PointF?): Boolean

    /**
     * Checks if the area contains a point defined by X and Y coordinates.
     *
     * @param x The X coordinate of the point.
     * @param y The Y coordinate of the point.
     * @return True if the area contains the point, false otherwise.
     */
    fun contains(x: Float, y: Float): Boolean

    /**
     * Checks if the area contains a given line.
     *
     * @param line The line to check.
     * @return True if the area contains the line, false otherwise.
     */
    fun contains(line: Line?): Boolean

    /**
     * Returns the path representing the area.
     */
    val areaPath: Path

    /**
     * Returns the rectangular bounds of the area as a RectF object.
     */
    val areaRect: RectF

    /**
     * Returns a list of lines that are part of the area.
     */
    val lines: List<Line>

    /**
     * Returns the handle bar points of a given line as an array of PointF objects.
     *
     * @param line The line for which to get the handle bar points.
     * @return An array of PointF representing the handle bar points.
     */
    fun getHandleBarPoints(line: Line): Array<PointF>

    /**
     * Returns the current rotation of the area in radians.
     */
    fun radian(): Float

    /**
     * Sets the rotation of the area to the specified radian value.
     *
     * @param radian The radian value to set.
     */
    fun setRadian(radian: Float)

    /**
     * Returns the left padding of the area.
     */
    val paddingLeft: Float

    /**
     * Returns the top padding of the area.
     */
    val paddingTop: Float

    /**
     * Returns the right padding of the area.
     */
    val paddingRight: Float

    /**
     * Returns the bottom padding of the area.
     */
    val paddingBottom: Float

    /**
     * Sets the padding for all sides of the area to the same value.
     *
     * @param padding The padding value to set for all sides.
     */
    fun setPadding(padding: Float)

    /**
     * Sets the padding for each side of the area individually.
     *
     * @param paddingLeft The left padding value.
     * @param paddingTop The top padding value.
     * @param paddingRight The right padding value.
     * @param paddingBottom The bottom padding value.
     */
    fun setPadding(paddingLeft: Float, paddingTop: Float, paddingRight: Float, paddingBottom: Float)
}





