package com.hypersoft.pzlayout.utils

import android.graphics.RectF
import com.hypersoft.pzlayout.interfaces.PuzzleLayout
import com.hypersoft.pzlayout.slant.SlantPuzzleLayout
import com.hypersoft.pzlayout.straight.StraightPuzzleLayout

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */
internal object PuzzleLayoutParser {

    /**
     * This function parses a `PuzzleLayout.Info` object and generates a corresponding `PuzzleLayout`.
     * It determines if the layout is a Straight or Slant layout and applies layout steps (like adding lines, crosses, or cutting areas).
     */
    @JvmStatic
    fun parse(info: PuzzleLayout.Info): PuzzleLayout {
        // If the type is straight, it creates an anonymous class for `StraightPuzzleLayout`.
        val layout = if (info.type == PuzzleLayout.Info.TYPE_STRAIGHT) {
            object : StraightPuzzleLayout() {
                // This function defines how the layout is constructed using the steps provided in `info`.
                override fun layout() {
                    val size = info.steps?.size
                    size?.let {
                        // Iterate over the steps and apply each one to the layout.
                        for (i in 0 until it) {
                            info.steps?.let { steps ->
                                val step = steps[i]

                                // Depending on the step type, add a line, cross, or cut the area into parts.
                                when (step.type) {
                                    PuzzleLayout.Step.ADD_LINE -> addLine(step.position, step.lineDirection(), 1f / 2)
                                    PuzzleLayout.Step.ADD_CROSS -> addCross(step.position, 1f / 2)
                                    PuzzleLayout.Step.CUT_EQUAL_PART_ONE -> cutAreaEqualPart(step.position, step.hSize, step.vSize)
                                    PuzzleLayout.Step.CUT_EQUAL_PART_TWO -> cutAreaEqualPart(step.position, step.part, step.lineDirection())
                                    PuzzleLayout.Step.CUT_SPIRAL -> cutSpiral(step.position)
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // If the type is slant, it creates an anonymous class for `SlantPuzzleLayout`.
            object : SlantPuzzleLayout() {
                // This function defines how the layout is constructed using the steps provided in `info`.
                override fun layout() {
                    val size = info.steps?.size
                    size?.let {
                        // Iterate over the steps and apply each one to the layout.
                        for (i in 0 until size) {
                            info.steps?.let { steps ->
                                val step = steps[i]

                                // Depending on the step type, add a line, cross, or cut the area.
                                when (step.type) {
                                    PuzzleLayout.Step.ADD_LINE -> addLine(step.position, step.lineDirection(), 1f / 2)
                                    PuzzleLayout.Step.ADD_CROSS -> addCross(step.position, 1f / 2, 1f / 2, 1f / 2, 1f / 2)
                                    PuzzleLayout.Step.CUT_EQUAL_PART_ONE -> cutArea(step.position, step.hSize, step.vSize)
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        }

        // Set the outer bounds of the layout using the rectangle defined in `info`.
        val bounds = RectF(info.left, info.top, info.right, info.bottom)
        layout.setOuterBounds(bounds)

        // Trigger the layout process by calling the overridden `layout()` method.
        layout.layout()

        // Apply other properties from `info` such as color, radian (rotation), and padding.
        layout.color = info.color
        layout.radian = info.radian
        layout.padding = info.padding

        // If line info is provided, map the start and end points of each line from `info` to the layout's lines.
        val size = info.lineInfos?.size
        size?.let {
            for (i in 0 until it) {
                info.lineInfos?.let { infoLine ->
                    val lineInfo = infoLine[i]
                    val line = layout.lines[i]
                    line.startPoint().x = lineInfo.startX
                    line.startPoint().y = lineInfo.startY
                    line.endPoint().x = lineInfo.endX
                    line.endPoint().y = lineInfo.endY
                }
            }
        }

        // Sort the layout areas to ensure they are in the correct order.
        layout.sortAreas()

        // Update the layout to finalize changes.
        layout.update()

        // Return the constructed layout.
        return layout
    }
}

