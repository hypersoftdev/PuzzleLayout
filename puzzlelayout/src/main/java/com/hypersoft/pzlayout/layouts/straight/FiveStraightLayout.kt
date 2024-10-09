package com.hypersoft.pzlayout.layouts.straight

import com.hypersoft.pzlayout.interfaces.Line

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */
class FiveStraightLayout(theme: Int) : NumberStraightLayout(theme) {

    override fun getThemeCount(): Int {
        return 19
    }

    override fun layout() {
        when (theme) {
            0 -> cutAreaEqualPart(0, 5, Line.Direction.HORIZONTAL)
            1 -> cutAreaEqualPart(0, 5, Line.Direction.VERTICAL)
            2 -> {
                addLine(0, Line.Direction.HORIZONTAL, 2f / 5)
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
                cutAreaEqualPart(2, 3, Line.Direction.VERTICAL)
            }

            3 -> {
                addLine(0, Line.Direction.HORIZONTAL, 3f / 5)
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
                addLine(3, Line.Direction.VERTICAL, 1f / 2)
            }

            4 -> {
                addLine(0, Line.Direction.VERTICAL, 2f / 5)
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 2)
            }

            5 -> {
                addLine(0, Line.Direction.VERTICAL, 2f / 5)
                cutAreaEqualPart(1, 3, Line.Direction.HORIZONTAL)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
            }

            6 -> {
                addLine(0, Line.Direction.HORIZONTAL, 3f / 4)
                cutAreaEqualPart(1, 4, Line.Direction.VERTICAL)
            }

            7 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 4)
                cutAreaEqualPart(0, 4, Line.Direction.VERTICAL)
            }

            8 -> {
                addLine(0, Line.Direction.VERTICAL, 3f / 4)
                cutAreaEqualPart(1, 4, Line.Direction.HORIZONTAL)
            }

            9 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 4)
                cutAreaEqualPart(0, 4, Line.Direction.HORIZONTAL)
            }

            10 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 4)
                addLine(1, Line.Direction.HORIZONTAL, 2f / 3)
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
                addLine(3, Line.Direction.VERTICAL, 1f / 2)
            }

            11 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 4)
                addLine(1, Line.Direction.VERTICAL, 2f / 3)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(2, Line.Direction.HORIZONTAL, 1f / 2)
            }

            12 -> {
                addCross(0, 1f / 3)
                addLine(2, Line.Direction.HORIZONTAL, 1f / 2)
            }

            13 -> {
                addCross(0, 2f / 3)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 2)
            }

            14 -> {
                addCross(0, 1f / 3, 2f / 3)
                addLine(3, Line.Direction.HORIZONTAL, 1f / 2)
            }

            15 -> {
                addCross(0, 2f / 3, 1f / 3)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
            }

            16 -> cutSpiral(0)

            17 -> {
                addLine(0, Line.Direction.HORIZONTAL, 2f / 3)
                cutAreaEqualPart(0, 1, 1)
            }

            18 -> {
                addLine(0, Line.Direction.VERTICAL, 2f / 3)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 3)
                addLine(0, Line.Direction.HORIZONTAL, 2f / 3)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 3)
            }

            else -> cutAreaEqualPart(0, 5, Line.Direction.HORIZONTAL)
        }
    }
}
