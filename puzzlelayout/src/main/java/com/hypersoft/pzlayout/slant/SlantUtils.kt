package com.hypersoft.pzlayout.slant

import android.graphics.PointF
import android.util.Pair
import com.hypersoft.pzlayout.interfaces.Line
import kotlin.math.abs
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
 * Utility object providing functions to manipulate SlantArea and create SlantLine instances.
 * This object includes methods for creating lines, cutting areas, and generating line patterns
 * for Slant puzzle layouts.
 */
internal object SlantUtils {
    private val A = PointF()
    private val B = PointF()
    private val C = PointF()
    private val D = PointF()
    private val AB = PointF()
    private val AM = PointF()
    private val BC = PointF()
    private val BM = PointF()
    private val CD = PointF()
    private val CM = PointF()
    private val DA = PointF()
    private val DM = PointF()

    /**
     * Calculates the Euclidean distance between two points.
     *
     * This function determines the distance between two given points in a 2D plane using the
     * Pythagorean theorem. The distance is the straight-line distance between the points.
     *
     * @param one The first point.
     * @param two The second point.
     * @return The Euclidean distance between the two points.
     */
    @JvmStatic
    fun distance(one: PointF, two: PointF): Float {
        return sqrt((two.x - one.x).toDouble().pow(2.0) + (two.y - one.y).toDouble().pow(2.0)).toFloat()
    }

    /**
     * Splits a given area into two parts using a provided slant line (horizontal or vertical).
     *
     * This function divides an area into two new areas using a specified slant line.
     * Depending on the line's direction (horizontal or vertical), the area is split
     * either along the top and bottom or the left and right.
     *
     * @param area The area to be split.
     * @param line The slant line that will divide the area.
     * @return A list containing two new areas resulting from the split.
     */
    @JvmStatic
    fun cutAreaWith(area: SlantArea?, line: SlantLine): List<SlantArea> {
        val areas: MutableList<SlantArea> = ArrayList()
        val area1 = SlantArea(area!!) // First part of the area after the split
        val area2 = SlantArea(area)   // Second part of the area after the split

        // If the line is horizontal, split the area along the top and bottom
        if (line.direction == Line.Direction.HORIZONTAL) {
            area1.lineBottom = line
            area1.leftBottom = line.start!!
            area1.rightBottom = line.end!!

            area2.lineTop = line
            area2.leftTop = line.start!!
            area2.rightTop = line.end!!
        }
        // If the line is vertical, split the area along the left and right
        else {
            area1.lineRight = line
            area1.rightTop = line.start!!
            area1.rightBottom = line.end!!

            area2.lineLeft = line
            area2.leftTop = line.start!!
            area2.leftBottom = line.end!!
        }

        // Add the two newly split areas to the list and return them
        areas.add(area1)
        areas.add(area2)

        return areas
    }


    /**
     * Creates a slant line within a specified area, either horizontal or vertical, based on start and end ratios.
     *
     * This function generates a slant line that spans from a start point to an end point within the area.
     * The location of the line is determined by the direction (horizontal or vertical) and
     * the start and end ratios which control where the line begins and ends.
     *
     * @param area The area within which the line will be created.
     * @param direction The direction of the line (either horizontal or vertical).
     * @param startRatio The ratio used to determine the starting position of the line within the area.
     * @param endRatio The ratio used to determine the ending position of the line within the area.
     * @return The created slant line with calculated start and end points.
     */
    @JvmStatic
    fun createLine(
        area: SlantArea, direction: Line.Direction, startRatio: Float,
        endRatio: Float,
    ): SlantLine {
        val line = SlantLine(direction)

        // If the line is horizontal, calculate the start and end points based on vertical ratios
        if (direction == Line.Direction.HORIZONTAL) {
            line.start = getPoint(area.leftTop, area.leftBottom, Line.Direction.VERTICAL, startRatio)
            line.end = getPoint(area.rightTop, area.rightBottom, Line.Direction.VERTICAL, endRatio)

            line.attachLineStart = area.lineLeft
            line.attachLineEnd = area.lineRight

            line.upperLine = area.lineBottom
            line.lowerLine = area.lineTop
        }
        // If the line is vertical, calculate the start and end points based on horizontal ratios
        else {
            line.start = getPoint(area.leftTop, area.rightTop, Line.Direction.HORIZONTAL, startRatio)
            line.end = getPoint(area.leftBottom, area.rightBottom, Line.Direction.HORIZONTAL, endRatio)

            line.attachLineStart = area.lineTop
            line.attachLineEnd = area.lineBottom

            line.upperLine = area.lineRight
            line.lowerLine = area.lineLeft
        }

        return line
    }

    /**
     * Divides a given slant area into a grid based on specified horizontal and vertical divisions.
     *
     * This function splits an area into smaller parts based on the specified number of horizontal and vertical cuts.
     * It first creates horizontal slant lines and divides the area, then adds vertical lines to further split
     * the area into smaller blocks. The function calculates the necessary intersection points between the lines.
     *
     * @param area The area to be divided into smaller blocks.
     * @param horizontalSize The number of horizontal divisions.
     * @param verticalSize The number of vertical divisions.
     * @return A pair consisting of a list of created slant lines and a list of resulting smaller areas.
     */
    @JvmStatic
    fun cutAreaWith(
        area: SlantArea?, horizontalSize: Int, verticalSize: Int,
    ): Pair<List<SlantLine>, List<SlantArea>> {
        val areaList: MutableList<SlantArea> = ArrayList()
        val horizontalLines: MutableList<SlantLine> = ArrayList(horizontalSize)

        var restArea = SlantArea(area!!)

        // Create horizontal lines to divide the area horizontally
        for (i in horizontalSize + 1 downTo 2) {
            val horizontalLine = createLine(
                restArea, Line.Direction.HORIZONTAL,
                (i - 1).toFloat() / i - 0.025f, (i - 1).toFloat() / i + 0.025f
            )
            horizontalLines.add(horizontalLine)

            // Update the bottom boundary of the restArea with the newly created horizontal line
            restArea.lineBottom = horizontalLine
            restArea.leftBottom = horizontalLine.start!!
            restArea.rightBottom = horizontalLine.end!!
        }

        val verticalLines: MutableList<SlantLine> = ArrayList()

        restArea = SlantArea(area)

        // Create vertical lines to divide the area vertically
        for (i in verticalSize + 1 downTo 2) {
            val verticalLine = createLine(
                restArea, Line.Direction.VERTICAL,
                (i - 1).toFloat() / i + 0.025f, (i - 1).toFloat() / i - 0.025f
            )
            verticalLines.add(verticalLine)

            val splitArea = SlantArea(restArea)
            splitArea.lineLeft = verticalLine
            splitArea.leftTop = verticalLine.start!!
            splitArea.leftBottom = verticalLine.end!!

            // Further split the areas using the horizontal lines
            for (j in 0..horizontalLines.size) {
                val blockArea = SlantArea(splitArea)
                when (j) {
                    0 -> {
                        blockArea.lineTop = horizontalLines[j]
                    }

                    horizontalLines.size -> {
                        blockArea.lineBottom = horizontalLines[j - 1]

                        // Calculate the intersection points for the bottom boundaries
                        val leftBottom = CrossoverPointF(blockArea.lineBottom, blockArea.lineLeft)
                        intersectionOfLines(leftBottom, blockArea.lineBottom!!, blockArea.lineLeft!!)
                        val rightBottom = CrossoverPointF(blockArea.lineBottom, blockArea.lineRight)
                        intersectionOfLines(rightBottom, blockArea.lineBottom!!, blockArea.lineRight!!)
                        blockArea.leftBottom = leftBottom
                        blockArea.rightBottom = rightBottom
                    }

                    else -> {
                        blockArea.lineTop = horizontalLines[j]
                        blockArea.lineBottom = horizontalLines[j - 1]
                    }
                }

                // Calculate the intersection points for the top boundaries
                val leftTop = CrossoverPointF(blockArea.lineTop, blockArea.lineLeft)
                intersectionOfLines(leftTop, blockArea.lineTop!!, blockArea.lineLeft!!)
                val rightTop = CrossoverPointF(blockArea.lineTop, blockArea.lineRight)
                intersectionOfLines(rightTop, blockArea.lineTop!!, blockArea.lineRight!!)
                blockArea.leftTop = leftTop
                blockArea.rightTop = rightTop
                areaList.add(blockArea)
            }

            // Update the right boundary of the restArea with the newly created vertical line
            restArea.lineRight = verticalLine
            restArea.rightTop = verticalLine.start!!
            restArea.rightBottom = verticalLine.end!!
        }

        // Process the final vertical slice of the area
        for (j in 0..horizontalLines.size) {
            val blockArea = SlantArea(restArea)
            when (j) {
                0 -> {
                    blockArea.lineTop = horizontalLines[j]
                }

                horizontalLines.size -> {
                    blockArea.lineBottom = horizontalLines[j - 1]

                    // Calculate the intersection points for the bottom boundaries
                    val leftBottom = CrossoverPointF(blockArea.lineBottom, blockArea.lineLeft)
                    intersectionOfLines(leftBottom, blockArea.lineBottom!!, blockArea.lineLeft!!)
                    val rightBottom = CrossoverPointF(blockArea.lineBottom, blockArea.lineRight)
                    intersectionOfLines(rightBottom, blockArea.lineBottom!!, blockArea.lineRight!!)
                    blockArea.leftBottom = leftBottom
                    blockArea.rightBottom = rightBottom
                }

                else -> {
                    blockArea.lineTop = horizontalLines[j]
                    blockArea.lineBottom = horizontalLines[j - 1]
                }
            }

            // Calculate the intersection points for the top boundaries
            val leftTop = CrossoverPointF(blockArea.lineTop, blockArea.lineLeft)
            intersectionOfLines(leftTop, blockArea.lineTop!!, blockArea.lineLeft!!)
            val rightTop = CrossoverPointF(blockArea.lineTop, blockArea.lineRight)
            intersectionOfLines(rightTop, blockArea.lineTop!!, blockArea.lineRight!!)
            blockArea.leftTop = leftTop
            blockArea.rightTop = rightTop
            areaList.add(blockArea)
        }

        // Combine all created lines and return them along with the divided areas
        val lines: MutableList<SlantLine> = ArrayList()
        lines.addAll(horizontalLines)
        lines.addAll(verticalLines)
        return Pair(lines, areaList)
    }

    @JvmStatic
    fun cutAreaCross(
        area: SlantArea?, horizontal: SlantLine,
        vertical: SlantLine,
    ): List<SlantArea> {
        // This function divides an area into four sub-areas by using two intersecting lines (horizontal and vertical).

        val list: MutableList<SlantArea> = ArrayList()

        // Determine the intersection point of the horizontal and vertical lines
        val crossoverPoint = CrossoverPointF(horizontal, vertical)
        intersectionOfLines(crossoverPoint, horizontal, vertical)

        // First sub-area (top-left)
        val one = SlantArea(area!!)
        one.lineBottom = horizontal
        one.lineRight = vertical
        one.rightTop = vertical.start!!
        one.rightBottom = crossoverPoint
        one.leftBottom = horizontal.start!!
        list.add(one)

        // Second sub-area (top-right)
        val two = SlantArea(area)
        two.lineBottom = horizontal
        two.lineLeft = vertical
        two.leftTop = vertical.start!!
        two.rightBottom = horizontal.end!!
        two.leftBottom = crossoverPoint
        list.add(two)

        // Third sub-area (bottom-left)
        val three = SlantArea(area)
        three.lineTop = horizontal
        three.lineRight = vertical
        three.leftTop = horizontal.start!!
        three.rightTop = crossoverPoint
        three.rightBottom = vertical.end!!
        list.add(three)

        // Fourth sub-area (bottom-right)
        val four = SlantArea(area)
        four.lineTop = horizontal
        four.lineLeft = vertical
        four.leftTop = crossoverPoint
        four.rightTop = horizontal.end!!
        four.leftBottom = vertical.end!!
        list.add(four)

        return list
    }

    private fun getPoint(
        start: PointF, end: PointF,
        direction: Line.Direction, ratio: Float,
    ): CrossoverPointF {
        // This function calculates a point along the line connecting start and end points,
        // based on the provided ratio. It returns the new calculated point.
        val point = CrossoverPointF()
        getPoint(point, start, end, direction, ratio)
        return point
    }

    @JvmStatic
    fun getPoint(
        dst: PointF, start: PointF, end: PointF,
        direction: Line.Direction, ratio: Float,
    ) {
        // This function computes a specific point on the line connecting two points (start, end)
        // based on the given ratio and direction (either horizontal or vertical).

        val deltaY = abs((start.y - end.y).toDouble()).toFloat()
        val deltaX = abs((start.x - end.x).toDouble()).toFloat()
        val maxY = max(start.y.toDouble(), end.y.toDouble()).toFloat()
        val minY = min(start.y.toDouble(), end.y.toDouble()).toFloat()
        val maxX = max(start.x.toDouble(), end.x.toDouble()).toFloat()
        val minX = min(start.x.toDouble(), end.x.toDouble()).toFloat()

        // Calculate position based on the line direction (HORIZONTAL or VERTICAL)
        if (direction == Line.Direction.HORIZONTAL) {
            dst.x = minX + deltaX * ratio
            dst.y = if (start.y < end.y) {
                minY + ratio * deltaY
            } else {
                maxY - ratio * deltaY
            }
        } else {
            dst.y = minY + deltaY * ratio
            dst.x = if (start.x < end.x) {
                minX + ratio * deltaX
            } else {
                maxX - ratio * deltaX
            }
        }
    }

    private fun crossProduct(a: PointF, b: PointF): Float {
        // This function calculates the cross product of two vectors (a and b).
        return a.x * b.y - b.x * a.y
    }

    @JvmStatic
    fun contains(area: SlantArea, x: Float, y: Float): Boolean {
        // This function checks whether a point (x, y) is contained within the given SlantArea.
        // It calculates the cross product for each side of the polygon and checks if the point
        // is inside by ensuring all cross products have the same sign.

        AB.x = area.rightTop.x - area.leftTop.x
        AB.y = area.rightTop.y - area.leftTop.y
        AM.x = x - area.leftTop.x
        AM.y = y - area.leftTop.y

        BC.x = area.rightBottom.x - area.rightTop.x
        BC.y = area.rightBottom.y - area.rightTop.y
        BM.x = x - area.rightTop.x
        BM.y = y - area.rightTop.y

        CD.x = area.leftBottom.x - area.rightBottom.x
        CD.y = area.leftBottom.y - area.rightBottom.y
        CM.x = x - area.rightBottom.x
        CM.y = y - area.rightBottom.y

        DA.x = area.leftTop.x - area.leftBottom.x
        DA.y = area.leftTop.y - area.leftBottom.y
        DM.x = x - area.leftBottom.x
        DM.y = y - area.leftBottom.y

        // Return true if the point is inside the polygon by ensuring all cross products are positive
        return crossProduct(AB, AM) > 0 && crossProduct(BC, BM) > 0 && crossProduct(CD, CM) > 0 && crossProduct(DA, DM) > 0
    }

    @JvmStatic
    fun contains(line: SlantLine, x: Float, y: Float, extra: Float): Boolean {
        // This function checks if a point (x, y) is within a certain boundary (extra tolerance) of a line (SlantLine).

        var start = PointF()
        var end = PointF()
        line.start?.let { ls -> start = ls }
        line.end?.let { le -> end = le }

        // Set up boundary for vertical lines
        if (line.direction == Line.Direction.VERTICAL) {
            A.x = start.x - extra
            A.y = start.y
            B.x = start.x + extra
            B.y = start.y
            C.x = end.x + extra
            C.y = end.y
            D.x = end.x - extra
            D.y = end.y
        } else {
            // Set up boundary for horizontal lines
            A.x = start.x
            A.y = start.y - extra
            B.x = end.x
            B.y = end.y - extra
            C.x = end.x
            C.y = end.y + extra
            D.x = start.x
            D.y = start.y + extra
        }

        // Calculate vectors for cross product to determine if point is inside
        AB.x = B.x - A.x
        AB.y = B.y - A.y
        AM.x = x - A.x
        AM.y = y - A.y

        BC.x = C.x - B.x
        BC.y = C.y - B.y
        BM.x = x - B.x
        BM.y = y - B.y

        CD.x = D.x - C.x
        CD.y = D.y - C.y
        CM.x = x - C.x
        CM.y = y - C.y

        DA.x = A.x - D.x
        DA.y = A.y - D.y
        DM.x = x - D.x
        DM.y = y - D.y

        // Return true if the point lies within the boundary created around the line
        return crossProduct(AB, AM) > 0 && crossProduct(BC, BM) > 0 && crossProduct(CD, CM) > 0 && crossProduct(DA, DM) > 0
    }

    @JvmStatic
    fun intersectionOfLines(
        dst: CrossoverPointF, lineOne: SlantLine,
        lineTwo: SlantLine,
    ) {
        // This function calculates the intersection point of two lines (SlantLines)
        // and stores the result in a CrossoverPointF object.

        dst.horizontal = lineOne
        dst.vertical = lineTwo

        // Check if lines are parallel
        if (isParallel(lineOne, lineTwo)) {
            dst[0f] = 0f
            return
        }

        // Handle cases where one line is horizontal and the other is vertical
        if (isHorizontalLine(lineOne) && isVerticalLine(lineTwo)) {
            lineTwo.start?.let { lts -> lineOne.start?.let { los -> dst[lts.x] = los.y; return } }
        }

        if (isVerticalLine(lineOne) && isHorizontalLine(lineTwo)) {
            lineTwo.start?.let { lts -> lineOne.start?.let { los -> dst[los.x] = lts.y; return } }
        }

        // Handle non-parallel cases using line slopes and intercepts
        val k1 = calculateSlope(lineOne)
        val b1 = calculateVerticalIntercept(lineOne)
        val k2 = calculateSlope(lineTwo)
        val b2 = calculateVerticalIntercept(lineTwo)

        // Calculate intersection point
        dst.x = (b2 - b1) / (k1 - k2)
        dst.y = dst.x * k1 + b1
    }

    private fun isHorizontalLine(line: SlantLine): Boolean {
        // Check if a line is horizontal by comparing the y-coordinates of its start and end points.
        val lStart = line.start?.y
        val lend = line.end?.y
        return lStart == lend
    }

    private fun isVerticalLine(line: SlantLine): Boolean {
        // Check if a line is vertical by comparing the x-coordinates of its start and end points.
        val lStart = line.start?.x
        val lend = line.end?.x
        return lStart == lend
    }

    private fun isParallel(lineOne: SlantLine, lineTwo: SlantLine): Boolean {
        // Determine if two lines are parallel by comparing their slopes.
        return calculateSlope(lineOne) == calculateSlope(lineTwo)
    }

    @JvmStatic
    fun calculateSlope(line: SlantLine): Float {
        // Calculate the slope of a line. Special cases for horizontal (slope = 0) and vertical (slope = ∞).
        return if (isHorizontalLine(line)) {
            0f
        } else if (isVerticalLine(line)) {
            Float.POSITIVE_INFINITY
        } else {
            (line.start!!.y - line.end!!.y) / (line.start!!.x - line.end!!.x)
        }
    }

    private fun calculateVerticalIntercept(line: SlantLine): Float {
        // Calculate the vertical intercept of a line (b in y = mx + b).
        if (isHorizontalLine(line)) {
            return line.start!!.y
        } else if (isVerticalLine(line)) {
            return Float.POSITIVE_INFINITY
        } else {
            val k = calculateSlope(line)
            return line.start!!.y - k * line.start!!.x
        }
    }


}
