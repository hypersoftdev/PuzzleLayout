package com.puzzle.layouts.layouts.straight

import com.puzzle.layouts.interfaces.Line

class ThreeStraightLayout(theme: Int) : NumberStraightLayout(theme) {

    override fun getThemeCount(): Int {
        return 8 // Updated to include additional themes
    }

    override fun layout() {
        when (theme) {
            0 -> cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
            1 -> cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
            2 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
            }

            3 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(1, Line.Direction.VERTICAL, 1f / 2)
            }

            4 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
            }

            5 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 2)
            }

            6 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 3)
                addLine(1, Line.Direction.VERTICAL, 1f / 2)
            }

            7 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 3)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 2)
            }

            else -> cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
        }
    }
}
