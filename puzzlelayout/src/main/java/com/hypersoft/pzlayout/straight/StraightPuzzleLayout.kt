package com.hypersoft.pzlayout.straight

import android.graphics.Color
import android.graphics.PointF
import android.graphics.RectF
import com.hypersoft.pzlayout.interfaces.Area
import com.hypersoft.pzlayout.interfaces.Line
import com.hypersoft.pzlayout.interfaces.PuzzleLayout
import com.hypersoft.pzlayout.straight.StraightUtils.createLine
import com.hypersoft.pzlayout.straight.StraightUtils.cutArea
import com.hypersoft.pzlayout.straight.StraightUtils.cutAreaCross
import com.hypersoft.pzlayout.straight.StraightUtils.cutAreaSpiral
import java.util.Collections

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

/**
 * Abstract class representing a straight puzzle layout.
 * This class manages the layout of puzzle areas and lines,
 * allowing for the addition of lines and manipulation of areas
 * in a structured manner.
 */
abstract class StraightPuzzleLayout protected constructor() : PuzzleLayout {
    // The bounding rectangle of the layout
    private var bounds: RectF? = null
    override var outerArea: StraightArea = StraightArea()

    // List of areas that make up the layout
    private val areas: MutableList<StraightArea> = ArrayList()

    // List of lines that define the layout structure
    override val lines: MutableList<Line> = ArrayList()

    // Outer lines of the puzzle layout
    override val outerLines: MutableList<Line> = ArrayList(4)

    // Padding value for the layout
    private var _padding: Float = 0f
    override var padding: Float
        get() = _padding
        set(value) {
            // Update padding for all areas and the outer lines
            this._padding = value

            for (area in areas) {
                area.setPadding(value)
            }

            // Update the start and end points of the outer lines based on the new padding
            outerArea.lineLeft!!.startPoint()[bounds!!.left + value] = bounds!!.top + value
            outerArea.lineLeft!!.endPoint()[bounds!!.left + value] = bounds!!.bottom - value
            outerArea.lineRight!!.startPoint()[bounds!!.right - value] = bounds!!.top + value
            outerArea.lineRight!!.endPoint()[bounds!!.right - value] = bounds!!.bottom - value

            update() // Update lines after padding change
        }

    // Rotation value for the layout
    private var _radian: Float = 0f
    override var radian: Float
        get() = _radian
        set(value) {
            this._radian = value
            // Apply the rotation to all areas
            for (area in areas) {
                area.setRadian(value)
            }
        }

    // Default color for the layout
    override var color: Int = Color.WHITE

    // Comparator for sorting areas
    private val areaComparator: Comparator<StraightArea> = StraightArea.AreaComparator()

    // List to track steps for undo/redo functionality
    private val steps = ArrayList<PuzzleLayout.Step>()

    /**
     * Sets the outer bounds of the layout and initializes the lines.
     * This should be called to define the area in which the puzzle is laid out.
     */
    override fun setOuterBounds(bounds: RectF) {
        reset() // Reset the layout to start fresh

        this.bounds = bounds

        // Define the corners of the outer bounds
        val one = PointF(bounds.left, bounds.top)
        val two = PointF(bounds.right, bounds.top)
        val three = PointF(bounds.left, bounds.bottom)
        val four = PointF(bounds.right, bounds.bottom)

        // Create lines for each edge of the outer bounds
        val lineLeft = StraightLine(one, three)
        val lineTop = StraightLine(one, two)
        val lineRight = StraightLine(two, four)
        val lineBottom = StraightLine(three, four)

        // Clear existing outer lines and set the new ones
        outerLines.clear()
        outerLines.add(lineLeft)
        outerLines.add(lineTop)
        outerLines.add(lineRight)
        outerLines.add(lineBottom)

        // Set the outer area lines
        outerArea.lineLeft = lineLeft
        outerArea.lineTop = lineTop
        outerArea.lineRight = lineRight
        outerArea.lineBottom = lineBottom

        // Initialize the areas with the outer area
        areas.clear()
        areas.add(outerArea)
    }

    /**
     * Abstract method to layout the puzzle.
     * Must be implemented in subclasses to define how areas and lines are arranged.
     */
    abstract override fun layout()

    /**
     * Returns the number of areas in the layout.
     */
    override val areaCount: Int
        get() = areas.size

    /**
     * Updates all lines in the layout based on the current width and height.
     */
    override fun update() {
        for (line in lines) {
            line.update(width(), height())
        }
    }

    /**
     * Returns the width of the outer area.
     */
    override fun width(): Float {
        return outerArea.width()
    }

    /**
     * Returns the height of the outer area.
     */
    override fun height(): Float {
        return outerArea.height()
    }

    /**
     * Resets the layout by clearing lines and areas and reinitializing the outer area.
     */
    override fun reset() {
        lines.clear()
        areas.clear()
        areas.add(outerArea)
        steps.clear()
    }

    /**
     * Returns the area at the specified position.
     */
    override fun getArea(position: Int): Area {
        return areas[position]
    }

    /**
     * Adds a line to the layout at the specified position and direction with a given ratio.
     * Also logs this action as a step for undo/redo functionality.
     */
    protected fun addLine(position: Int, direction: Line.Direction, ratio: Float) {
        val area = areas[position]
        addLine(area, direction, ratio)

        val step = PuzzleLayout.Step()
        step.type = PuzzleLayout.Step.ADD_LINE
        step.direction = if (direction == Line.Direction.HORIZONTAL) 0 else 1
        step.position = position
        steps.add(step)
    }

    /**
     * Adds a line to the specified area and cuts the area into smaller areas.
     * Updates line limits and sorts the areas after modification.
     */
    private fun addLine(area: StraightArea, direction: Line.Direction, ratio: Float): List<StraightArea> {
        areas.remove(area)
        val line = createLine(area, direction, ratio)
        lines.add(line)

        val increasedArea = cutArea(area, line)
        areas.addAll(increasedArea)

        updateLineLimit() // Update limits for the newly added line
        sortAreas() // Sort areas based on their position

        return increasedArea
    }

    /**
     * Cuts the specified area into equal parts by adding lines in the given direction.
     */
    protected fun cutAreaEqualPart(position: Int, part: Int, direction: Line.Direction) {
        var temp = areas[position]
        for (i in part downTo 2) {
            temp = addLine(temp, direction, (i - 1).toFloat() / i)[0]
        }

        val step = PuzzleLayout.Step()
        step.type = PuzzleLayout.Step.CUT_EQUAL_PART_TWO
        step.part = part
        step.position = position
        step.direction = if (direction == Line.Direction.HORIZONTAL) 0 else 1
        steps.add(step)
    }

    /**
     * Adds a cross line (both horizontal and vertical) to the specified area.
     */
    protected fun addCross(position: Int, ratio: Float) {
        addCross(position, ratio, ratio)
    }

    /**
     * Adds a cross line to the specified area with specified horizontal and vertical ratios.
     */
    protected fun addCross(position: Int, horizontalRatio: Float, verticalRatio: Float) {
        val area = areas[position]
        areas.remove(area)
        val horizontal = createLine(area, Line.Direction.HORIZONTAL, horizontalRatio)
        val vertical = createLine(area, Line.Direction.VERTICAL, verticalRatio)
        lines.add(horizontal)
        lines.add(vertical)

        val newAreas = cutAreaCross(area, horizontal, vertical)
        areas.addAll(newAreas)

        updateLineLimit() // Update limits for new lines
        sortAreas() // Sort areas after addition

        val step = PuzzleLayout.Step()
        step.type = PuzzleLayout.Step.ADD_CROSS
        step.position = position
        steps.add(step)
    }

    /**
     * Cuts the specified area into equal parts horizontally and vertically.
     */
    protected fun cutAreaEqualPart(position: Int, hSize: Int, vSize: Int) {
        val area = areas[position]
        areas.remove(area)
        val increased = cutArea(area, hSize, vSize)
        val newLines = increased.first
        val newAreas = increased.second

        lines.addAll(newLines)
        areas.addAll(newAreas)

        updateLineLimit() // Update limits for new lines
        sortAreas() // Sort areas after modification

        val step = PuzzleLayout.Step()
        step.type = PuzzleLayout.Step.CUT_EQUAL_PART_ONE
        step.position = position
        step.hSize = hSize
        step.vSize = vSize
        steps.add(step)
    }

    /**
     * Cuts the specified area into a spiral pattern.
     */
    protected fun cutSpiral(position: Int) {
        val area = areas[position]
        areas.remove(area)
        val spilt = cutAreaSpiral(area)

        lines.addAll(spilt.first)
        areas.addAll(spilt.second)

        updateLineLimit() // Update limits for new lines
        sortAreas() // Sort areas after modification

        val step = PuzzleLayout.Step()
        step.type = PuzzleLayout.Step.CUT_SPIRAL
        step.position = position
        steps.add(step)
    }

    /**
     * Sorts the areas based on the defined area comparator.
     */
    override fun sortAreas() {
        Collections.sort(areas, areaComparator)
    }

    /**
     * Updates the limits for all lines in the layout.
     */
    private fun updateLineLimit() {
        for (i in lines.indices) {
            val line = lines[i]
            updateUpperLine(line) // Update upper line relationships
            updateLowerLine(line) // Update lower line relationships
        }
    }

    /**
     * Updates the lower line relationship for the specified line.
     */
    private fun updateLowerLine(line: Line) {
        for (i in lines.indices) {
            val l = lines[i]
            if (l === line) continue // Skip the same line

            if (l.direction() != line.direction()) continue // Skip lines in different directions

            if (l.direction() == Line.Direction.HORIZONTAL) {
                if (l.maxX() <= line.minX() || line.maxX() <= l.minX()) continue
                if (l.minY() > line.lowerLine().maxY() && l.maxY() < line.minY()) {
                    line.setLowerLine(l)
                }
            } else {
                if (l.maxY() <= line.minY() || line.maxY() <= l.minY()) continue
                if (l.minX() > line.lowerLine().maxX() && l.maxX() < line.minX()) {
                    line.setLowerLine(l)
                }
            }
        }
    }

    /**
     * Updates the upper line relationship for the specified line.
     */
    private fun updateUpperLine(line: Line) {
        for (i in lines.indices) {
            val l = lines[i]
            if (l === line) continue // Skip the same line

            if (l.direction() != line.direction()) continue // Skip lines in different directions

            if (l.direction() == Line.Direction.HORIZONTAL) {
                if (l.maxX() <= line.minX() || line.maxX() <= l.minX()) continue
                if (l.maxY() < line.upperLine().minY() && l.minY() > line.maxY()) {
                    line.setUpperLine(l)
                }
            } else {
                if (l.maxY() <= line.minY() || line.maxY() <= l.minY()) continue
                if (l.maxX() < line.upperLine().minX() && l.minX() > line.maxX()) {
                    line.setUpperLine(l)
                }
            }
        }
    }

    /**
     * Generates and returns information about the puzzle layout, including
     * its type, padding, rotation, color, steps, and line information.
     */
    override fun generateInfo(): PuzzleLayout.Info {
        val info = PuzzleLayout.Info()
        info.type = PuzzleLayout.Info.TYPE_STRAIGHT
        info.padding = padding
        info.radian = radian
        info.color = color
        info.steps = steps
        val lineInfos = ArrayList<PuzzleLayout.LineInfo>()
        for (line in lines) {
            val lineInfo = PuzzleLayout.LineInfo(line)
            lineInfos.add(lineInfo)
        }
        info.lineInfos = lineInfos

        // Set bounds information
        info.left = bounds!!.left
        info.top = bounds!!.top
        info.right = bounds!!.right
        info.bottom = bounds!!.bottom

        return info
    }
}
