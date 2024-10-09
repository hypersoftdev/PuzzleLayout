package com.hypersoft.pzlayout.straight

import android.graphics.PointF
import android.util.Pair
import com.hypersoft.pzlayout.interfaces.Line

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

/**
 * Utility object providing functions to manipulate StraightArea and create StraightLine instances.
 * This object includes methods for creating lines, cutting areas, and generating line patterns
 * for straight puzzle layouts.
 */
internal object StraightUtils {

    /**
     * Creates a horizontal or vertical line within a given area based on the specified ratio.
     *
     * @param area The area within which to create the line.
     * @param direction The direction of the line (horizontal or vertical).
     * @param ratio The ratio to determine the line's position within the area.
     * @return The created StraightLine.
     */
    @JvmStatic
    fun createLine(
        area: StraightArea, direction: Line.Direction,
        ratio: Float,
    ): StraightLine {
        val one = PointF() // Start point of the line
        val two = PointF() // End point of the line

        // Set line coordinates based on direction
        if (direction == Line.Direction.HORIZONTAL) {
            one.x = area.left()
            one.y = area.height() * ratio + area.top()
            two.x = area.right()
            two.y = area.height() * ratio + area.top()
        } else if (direction == Line.Direction.VERTICAL) {
            one.x = area.width() * ratio + area.left()
            one.y = area.top()
            two.x = area.width() * ratio + area.left()
            two.y = area.bottom()
        }

        val line = StraightLine(one, two)

        // Attach the created line to the respective area lines
        if (direction == Line.Direction.HORIZONTAL) {
            line.attachLineStart = area.lineLeft
            line.attachLineEnd = area.lineRight
            line.setUpperLine(area.lineBottom!!)
            line.setLowerLine(area.lineTop!!)
        } else if (direction == Line.Direction.VERTICAL) {
            line.attachLineStart = area.lineTop
            line.attachLineEnd = area.lineBottom
            line.setUpperLine(area.lineRight!!)
            line.setLowerLine(area.lineLeft!!)
        }

        return line
    }

    /**
     * Cuts an area into two separate areas based on a horizontal or vertical line.
     *
     * @param area The area to be cut.
     * @param line The line used to cut the area.
     * @return A list of newly created StraightAreas.
     */
    @JvmStatic
    fun cutArea(area: StraightArea?, line: StraightLine): List<StraightArea> {
        val list: MutableList<StraightArea> = ArrayList()
        if (line.direction() == Line.Direction.HORIZONTAL) {
            val one = StraightArea(area!!)
            one.lineBottom = line // Set the bottom line for the new area
            list.add(one)

            val two = StraightArea(area)
            two.lineTop = line // Set the top line for the new area
            list.add(two)
        } else if (line.direction() == Line.Direction.VERTICAL) {
            val one = StraightArea(area!!)
            one.lineRight = line // Set the right line for the new area
            list.add(one)

            val two = StraightArea(area)
            two.lineLeft = line // Set the left line for the new area
            list.add(two)
        }

        return list // Return the list of newly created areas
    }

    /**
     * Cuts an area into multiple areas based on specified horizontal and vertical sizes.
     *
     * @param area The area to be cut.
     * @param horizontalSize The number of horizontal segments.
     * @param verticalSize The number of vertical segments.
     * @return A pair containing the created lines and areas.
     */
    @JvmStatic
    fun cutArea(
        area: StraightArea?,
        horizontalSize: Int, verticalSize: Int,
    ): Pair<List<StraightLine>, List<StraightArea>> {
        val areaList: MutableList<StraightArea> = ArrayList()
        val horizontalLines: MutableList<StraightLine> = ArrayList(horizontalSize)

        var restArea = StraightArea(area!!)
        // Create horizontal lines within the area
        for (i in horizontalSize + 1 downTo 2) {
            val horizontalLine =
                createLine(restArea, Line.Direction.HORIZONTAL, (i - 1).toFloat() / i)
            horizontalLines.add(horizontalLine)
            restArea.lineBottom = horizontalLine // Update the rest area bottom line
        }

        val verticalLines: MutableList<StraightLine> = ArrayList()
        restArea = StraightArea(area)
        // Create vertical lines within the area
        for (i in verticalSize + 1 downTo 2) {
            val verticalLine =
                createLine(restArea, Line.Direction.VERTICAL, (i - 1).toFloat() / i)
            verticalLines.add(verticalLine)
            val spiltArea = StraightArea(restArea)
            spiltArea.lineLeft = verticalLine // Set the left line for the spilt area

            // Create block areas based on the vertical line and horizontal lines
            for (j in 0..horizontalLines.size) {
                val blockArea = StraightArea(spiltArea)
                when (j) {
                    0 -> {
                        blockArea.lineTop = horizontalLines[j] // Set top line for the first block
                    }

                    horizontalLines.size -> {
                        blockArea.lineBottom = horizontalLines[j - 1] // Set bottom line for the last block
                    }

                    else -> {
                        blockArea.lineTop = horizontalLines[j]
                        blockArea.lineBottom = horizontalLines[j - 1]
                    }
                }
                areaList.add(blockArea)
            }
            restArea.lineRight = verticalLine // Update the rest area right line
        }

        // Create final block areas after vertical line creation
        for (j in 0..horizontalLines.size) {
            val blockArea = StraightArea(restArea)
            when (j) {
                0 -> {
                    blockArea.lineTop = horizontalLines[j] // Set top line for the first block
                }

                horizontalLines.size -> {
                    blockArea.lineBottom = horizontalLines[j - 1] // Set bottom line for the last block
                }

                else -> {
                    blockArea.lineTop = horizontalLines[j]
                    blockArea.lineBottom = horizontalLines[j - 1]
                }
            }
            areaList.add(blockArea)
        }

        // Combine horizontal and vertical lines into a single list
        val lines: MutableList<StraightLine> = ArrayList()
        lines.addAll(horizontalLines)
        lines.addAll(verticalLines)
        return Pair(lines, areaList) // Return the created lines and areas
    }

    /**
     * Cuts an area into four sections using specified horizontal and vertical lines.
     *
     * @param area The area to be cut.
     * @param horizontal The horizontal line used for cutting.
     * @param vertical The vertical line used for cutting.
     * @return A list of newly created StraightAreas.
     */
    @JvmStatic
    fun cutAreaCross(
        area: StraightArea?, horizontal: StraightLine?,
        vertical: StraightLine?,
    ): List<StraightArea> {
        val list: MutableList<StraightArea> = ArrayList()

        // Create four areas based on the intersection of the horizontal and vertical lines
        val one = StraightArea(area!!)
        one.lineBottom = horizontal
        one.lineRight = vertical
        list.add(one)

        val two = StraightArea(area)
        two.lineBottom = horizontal
        two.lineLeft = vertical
        list.add(two)

        val three = StraightArea(area)
        three.lineTop = horizontal
        three.lineRight = vertical
        list.add(three)

        val four = StraightArea(area)
        four.lineTop = horizontal
        four.lineLeft = vertical
        list.add(four)

        return list // Return the list of newly created areas
    }

    /**
     * Cuts an area into a spiral pattern, creating lines and subdivided areas.
     *
     * @param area The area to be cut into a spiral pattern.
     * @return A pair containing the created lines and areas.
     */
    @JvmStatic
    fun cutAreaSpiral(area: StraightArea): Pair<List<StraightLine>, List<StraightArea>> {
        val lines: MutableList<StraightLine> = ArrayList()
        val areas: MutableList<StraightArea> = ArrayList()

        val width = area.width()
        val height = area.height()

        // Define points for the spiral cut
        val left = area.left()
        val top = area.top()

        val one = PointF(left, top + height / 3)
        val two = PointF(left + width / 3 * 2, top)
        val three = PointF(left + width, top + height / 3 * 2)
        val four = PointF(left + width / 3, top + height)
        val five = PointF(left + width / 3, top + height / 3)
        val six = PointF(left + width / 3 * 2, top + height / 3)
        val seven = PointF(left + width / 3 * 2, top + height / 3 * 2)
        val eight = PointF(left + width / 3, top + height / 3 * 2)

        // Create lines for the spiral cut
        val l1 = StraightLine(one, six)
        val l2 = StraightLine(two, seven)
        val l3 = StraightLine(eight, three)
        val l4 = StraightLine(five, four)

        // Attach lines and set upper/lower connections
        l1.setAttachLineStart(area.lineLeft)
        l1.setAttachLineEnd(l2)
        l1.setLowerLine(area.lineTop!!)
        l1.setUpperLine(l3)

        l2.setAttachLineStart(area.lineTop)
        l2.setAttachLineEnd(l3)
        l2.setLowerLine(l4)
        l2.setUpperLine(area.lineRight!!)

        l3.setAttachLineStart(l4)
        l3.setAttachLineEnd(area.lineRight)
        l3.setLowerLine(l1)
        l3.setUpperLine(area.lineBottom!!)

        l4.setAttachLineStart(l1)
        l4.setAttachLineEnd(area.lineBottom)
        l4.setLowerLine(area.lineLeft!!)
        l4.setUpperLine(l2)

        // Add created lines to the list
        lines.add(l1)
        lines.add(l2)
        lines.add(l3)
        lines.add(l4)

        // Create and add areas based on the spiral cut
        val b1 = StraightArea(area)
        b1.lineRight = l2
        b1.lineBottom = l1
        areas.add(b1)

        val b2 = StraightArea(area)
        b2.lineLeft = l2
        b2.lineBottom = l3
        areas.add(b2)

        val b3 = StraightArea(area)
        b3.lineRight = l4
        b3.lineTop = l1
        areas.add(b3)

        val b4 = StraightArea(area)
        b4.lineTop = l1
        b4.lineRight = l2
        b4.lineLeft = l4
        b4.lineBottom = l3
        areas.add(b4)

        val b5 = StraightArea(area)
        b5.lineLeft = l4
        b5.lineTop = l3
        areas.add(b5)

        return Pair(lines, areas) // Return the created lines and areas
    }
}
