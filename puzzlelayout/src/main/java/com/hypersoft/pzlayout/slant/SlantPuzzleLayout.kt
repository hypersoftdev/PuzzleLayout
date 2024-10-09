package com.hypersoft.pzlayout.slant

import android.graphics.Color
import android.graphics.RectF
import com.hypersoft.pzlayout.interfaces.Line
import com.hypersoft.pzlayout.interfaces.PuzzleLayout
import com.hypersoft.pzlayout.slant.SlantUtils.createLine
import com.hypersoft.pzlayout.slant.SlantUtils.cutAreaCross
import com.hypersoft.pzlayout.slant.SlantUtils.cutAreaWith
import java.util.Collections

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

/**
 * An abstract class representing a layout for slant puzzle pieces.
 * It extends the PuzzleLayout interface and provides methods to manage
 * slanted lines and areas within the layout.
 */
abstract class SlantPuzzleLayout protected constructor() : PuzzleLayout {
    private var bounds: RectF? = null // The bounding rectangle for the layout
    override var outerArea = SlantArea() // The outer area of the layout

    // List of outer lines defining the boundaries of the layout
    override val outerLines: MutableList<Line> = ArrayList(4)
    private val areas: MutableList<SlantArea> = ArrayList() // Areas within the layout
    override val lines: MutableList<Line> = ArrayList() // Lines within the layout

    private var _padding: Float = 0f // Padding for the layout
    override var padding: Float
        get() {
            return _padding // Return the current padding value
        }
        set(value) {
            this._padding = value // Set the new padding value
            for (area in areas) {
                area.setPadding(value) // Update padding for each area
            }

            // Adjust outer area lines based on new padding
            outerArea.lineLeft!!.startPoint()[bounds!!.left + value] = bounds!!.top + value
            outerArea.lineLeft!!.endPoint()[bounds!!.left + value] = bounds!!.bottom - value

            outerArea.lineRight!!.startPoint()[bounds!!.right - value] = bounds!!.top + value
            outerArea.lineRight!!.endPoint()[bounds!!.right - value] = bounds!!.bottom - value

            outerArea.updateCornerPoints() // Update corner points of the outer area
            update() // Update the layout after padding change
        }

    private var _radian: Float = 0f // Rotation angle for the layout
    override var radian: Float
        get() {
            return _radian // Return the current radian value
        }
        set(value) {
            this._radian = value // Set the new radian value
            for (area in areas) {
                area.setRadian(value) // Update rotation for each area
            }
        }

    override var color: Int = Color.WHITE // Color of the layout

    // Comparator for sorting areas
    private val areaComparator: Comparator<SlantArea> = SlantArea.AreaComparator()
    private val steps = ArrayList<PuzzleLayout.Step>() // Steps taken in the layout

    // Set the outer bounds of the layout and initialize lines and areas
    override fun setOuterBounds(bounds: RectF) {
        reset() // Reset any existing layout elements

        this.bounds = bounds // Store the new bounds

        // Create crossover points for the corners of the bounds
        val leftTop = CrossoverPointF(bounds.left, bounds.top)
        val rightTop = CrossoverPointF(bounds.right, bounds.top)
        val leftBottom = CrossoverPointF(bounds.left, bounds.bottom)
        val rightBottom = CrossoverPointF(bounds.right, bounds.bottom)

        // Create outer lines based on the bounds
        val lineLeft = SlantLine(leftTop, leftBottom, Line.Direction.VERTICAL)
        val lineTop = SlantLine(leftTop, rightTop, Line.Direction.HORIZONTAL)
        val lineRight = SlantLine(rightTop, rightBottom, Line.Direction.VERTICAL)
        val lineBottom = SlantLine(leftBottom, rightBottom, Line.Direction.HORIZONTAL)

        outerLines.clear() // Clear existing outer lines

        // Add new outer lines to the layout
        outerLines.add(lineLeft)
        outerLines.add(lineTop)
        outerLines.add(lineRight)
        outerLines.add(lineBottom)

        // Set the outer lines to the outer area
        outerArea.lineLeft = lineLeft
        outerArea.lineTop = lineTop
        outerArea.lineRight = lineRight
        outerArea.lineBottom = lineBottom

        outerArea.updateCornerPoints() // Update corner points for the outer area

        areas.clear() // Clear existing areas
        areas.add(outerArea) // Add the outer area as the first area
    }

    // Abstract function for laying out the puzzle elements, must be implemented by subclasses
    abstract override fun layout()

    // Update the limits of the lines based on their positions
    private fun updateLineLimit() {
        for (i in lines.indices) {
            val line = lines[i]
            updateUpperLine(line) // Update the upper line reference
            updateLowerLine(line) // Update the lower line reference
        }
    }

    // Update the lower line reference for the given line
    private fun updateLowerLine(line: Line) {
        for (i in lines.indices) {
            val l = lines[i]
            if (l.direction() != line.direction()) {
                continue // Skip lines with different directions
            }

            // Check if the attached start and end lines are the same
            if (l.attachStartLine() !== line.attachStartLine() || l.attachEndLine() !== line.attachEndLine()) {
                continue
            }

            // Update lower line for horizontal direction
            if (l.direction() == Line.Direction.HORIZONTAL) {
                if (l.minY() > line.lowerLine().maxY() && l.maxY() < line.minY()) {
                    line.setLowerLine(l) // Set the lower line
                }
            } else { // Update lower line for vertical direction
                if (l.minX() > line.lowerLine().maxX() && l.maxX() < line.minX()) {
                    line.setLowerLine(l) // Set the lower line
                }
            }
        }
    }

    // Update the upper line reference for the given line
    private fun updateUpperLine(line: Line) {
        for (i in lines.indices) {
            val l = lines[i]
            if (l.direction() != line.direction()) {
                continue // Skip lines with different directions
            }

            // Check if the attached start and end lines are the same
            if (l.attachStartLine() !== line.attachStartLine() || l.attachEndLine() !== line.attachEndLine()) {
                continue
            }

            // Update upper line for horizontal direction
            if (l.direction() == Line.Direction.HORIZONTAL) {
                if (l.maxY() < line.upperLine().minY() && l.minY() > line.maxY()) {
                    line.setUpperLine(l) // Set the upper line
                }
            } else { // Update upper line for vertical direction
                if (l.maxX() < line.upperLine().minX() && l.minX() > line.maxX()) {
                    line.setUpperLine(l) // Set the upper line
                }
            }
        }
    }

    override val areaCount: Int
        get() = areas.size // Return the count of areas

    // Reset the layout by clearing lines, areas, and steps
    override fun reset() {
        lines.clear() // Clear all lines
        areas.clear() // Clear all areas
        areas.add(outerArea) // Re-add the outer area
        steps.clear() // Clear all steps
    }

    // Update the positions of lines and areas
    override fun update() {
        for (i in lines.indices) {
            lines[i].update(width(), height()) // Update each line
        }

        for (i in areas.indices) {
            areas[i].updateCornerPoints() // Update corner points for each area
        }
    }

    // Sort the areas using the defined area comparator
    override fun sortAreas() {
        Collections.sort(areas, areaComparator) // Sort areas
    }

    override fun width(): Float {
        return outerArea.width() // Return the width of the outer area
    }

    override fun height(): Float {
        return outerArea.height() // Return the height of the outer area
    }

    // Get the specified area by its position
    override fun getArea(position: Int): SlantArea {
        sortAreas() // Ensure areas are sorted before accessing
        return areas[position] // Return the area at the specified position
    }

    // Add a slant line to the layout at a specified position with a direction and ratio
    protected fun addLine(position: Int, direction: Line.Direction, ratio: Float): List<SlantArea> {
        return addLine(position, direction, ratio, ratio) // Call overload with the same ratio for both ends
    }

    // Add a slant line with specified start and end ratios at a given position
    protected fun addLine(position: Int, direction: Line.Direction, startRatio: Float, endRatio: Float): List<SlantArea> {
        val area = areas[position] // Get the area at the specified position
        areas.remove(area) // Remove the area for modification
        val line = createLine(area, direction, startRatio, endRatio) // Create a new slant line
        lines.add(line) // Add the new line to the layout

        // Cut the area based on the new line and get increased areas
        val increasedAreas = cutAreaWith(area, line)

        areas.addAll(increasedAreas) // Add the new areas to the layout

        updateLineLimit() // Update the line limits
        sortAreas() // Sort the areas

        // Create a step for adding a line
        val step = PuzzleLayout.Step()
        step.type = PuzzleLayout.Step.ADD_LINE
        step.direction = if (direction == Line.Direction.HORIZONTAL) 0 else 1 // Set the direction
        step.position = position // Set the position
        steps.add(step) // Add the step to the list

        return increasedAreas // Return the new areas created by the addition
    }

    // (Optional: Add a cross section in the layout. Currently not used based on the context.)
    protected fun addCross(position: Int, startRatio1: Float, endRatio1: Float, startRatio2: Float, endRatio2: Float) {
        val area = areas[position] // Get the area at the specified position
        areas.remove(area) // Remove the area for modification

        // Create horizontal and vertical lines
        val horizontal = createLine(area, Line.Direction.HORIZONTAL, startRatio1, endRatio1)
        val vertical = createLine(area, Line.Direction.VERTICAL, startRatio2, endRatio2)
        lines.add(horizontal) // Add the horizontal line
        lines.add(vertical) // Add the vertical line

        // Cut the area based on the new lines and get increased areas
        val increasedAreas = cutAreaCross(area, horizontal, vertical)

        areas.addAll(increasedAreas) // Add the new areas to the layout
        sortAreas() // Sort the areas

        // Create a step for adding a cross
        val step = PuzzleLayout.Step()
        step.type = PuzzleLayout.Step.ADD_CROSS
        step.position = position // Set the position
        steps.add(step) // Add the step to the list
    }

    // Cut an area into smaller parts based on specified sizes
    protected fun cutArea(position: Int, hSize: Int, vSize: Int) {
        val area = areas[position] // Get the area at the specified position
        areas.remove(area) // Remove the area for modification

        // Cut the area into smaller parts and retrieve the resulting lines and areas
        val spilt = cutAreaWith(area, hSize, vSize)

        lines.addAll(spilt.first) // Add new lines to the layout
        areas.addAll(spilt.second) // Add new areas to the layout

        updateLineLimit() // Update the line limits
        sortAreas() // Sort the areas

        // Create a step for cutting the area
        val step = PuzzleLayout.Step()
        step.type = PuzzleLayout.Step.CUT_EQUAL_PART_ONE
        step.position = position // Set the position
        step.hSize = hSize // Set the horizontal size
        step.vSize = vSize // Set the vertical size
        steps.add(step) // Add the step to the list
    }

    // Generate information about the current layout state
    override fun generateInfo(): PuzzleLayout.Info {
        val info = PuzzleLayout.Info() // Create a new Info object
        info.type = PuzzleLayout.Info.TYPE_SLANT // Set the type to slant
        info.padding = padding // Set the padding
        info.radian = radian // Set the rotation angle
        info.color = color // Set the color
        info.steps = steps // Set the steps taken

        // Prepare line information
        val lineInfos = ArrayList<PuzzleLayout.LineInfo>()
        for (line in lines) {
            val lineInfo = PuzzleLayout.LineInfo(line) // Create line info for each line
            lineInfos.add(lineInfo) // Add it to the list
        }
        info.lineInfos = lineInfos // Add line information to the info object

        // Set bounding rectangle information
        info.left = bounds!!.left
        info.top = bounds!!.top
        info.right = bounds!!.right
        info.bottom = bounds!!.bottom

        return info // Return the generated information
    }
}
