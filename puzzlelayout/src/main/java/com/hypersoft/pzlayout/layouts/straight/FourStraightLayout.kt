package com.hypersoft.pzlayout.layouts.straight

import com.hypersoft.pzlayout.interfaces.Line

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */
class FourStraightLayout(theme: Int) : NumberStraightLayout(theme) {

    override fun getThemeCount(): Int {
        return 12 // Updated to include additional themes
    }

    override fun layout() {
        when (theme) {
            0 -> cutAreaEqualPart(0, 4, Line.Direction.HORIZONTAL)
            1 -> cutAreaEqualPart(0, 4, Line.Direction.VERTICAL)
            2 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
            }

            3 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 3)
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
            }

            4 -> {
                addLine(0, Line.Direction.HORIZONTAL, 2f / 3)
                cutAreaEqualPart(1, 3, Line.Direction.VERTICAL)
            }

            5 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 3)
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
            }

            6 -> {
                addLine(0, Line.Direction.VERTICAL, 2f / 3)
                cutAreaEqualPart(1, 3, Line.Direction.HORIZONTAL)
            }

            7 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
                addLine(1, Line.Direction.HORIZONTAL, 2f / 3)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 3)
            }

            8 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 4)
                addLine(0, Line.Direction.HORIZONTAL, 3f / 4)
                addLine(1, Line.Direction.VERTICAL, 1f / 2)
            }

            9 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 4)
                addLine(0, Line.Direction.VERTICAL, 3f / 4)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 2)
            }

            10 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
                cutAreaEqualPart(0, 2, Line.Direction.HORIZONTAL)
            }

            11 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 3)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
            }

            else -> cutAreaEqualPart(0, 4, Line.Direction.HORIZONTAL)
        }
    }
}

