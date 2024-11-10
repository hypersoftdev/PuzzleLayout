package com.hypersoft.pzlayout.layouts.straight

import com.hypersoft.pzlayout.interfaces.Line

class SevenStraightLayout(theme: Int) : NumberStraightLayout(theme) {

    override fun getThemeCount(): Int {
        return 9
    }

    override fun layout() {
        when (theme) {
            0 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
                cutAreaEqualPart(1, 4, Line.Direction.VERTICAL)
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
            }

            1 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
                cutAreaEqualPart(1, 4, Line.Direction.HORIZONTAL)
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
            }

            2 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
                cutAreaEqualPart(1, 1, 2)
            }

            3 -> {
                addLine(0, Line.Direction.HORIZONTAL, 2f / 3)
                cutAreaEqualPart(1, 3, Line.Direction.VERTICAL)
                addCross(0, 1f / 2)
            }

            4 -> {
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
                cutAreaEqualPart(2, 3, Line.Direction.HORIZONTAL)
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
            }

            5 -> {
                addLine(0, Line.Direction.HORIZONTAL, 2f / 3)
                addLine(1, Line.Direction.VERTICAL, 3f / 4)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(1, Line.Direction.VERTICAL, 2f / 5)
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
            }

            6 -> {
                addLine(0, Line.Direction.VERTICAL, 2f / 3)
                addLine(1, Line.Direction.HORIZONTAL, 3f / 4)
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
                addLine(1, Line.Direction.HORIZONTAL, 2f / 5)
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
            }

            7 -> {
                addLine(0, Line.Direction.VERTICAL, 1f / 4)
                addLine(1, Line.Direction.VERTICAL, 2f / 3)
                addLine(2, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(1, Line.Direction.HORIZONTAL, 3f / 4)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 3)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
            }

            8 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 4)
                addLine(1, Line.Direction.HORIZONTAL, 2f / 3)
                cutAreaEqualPart(2, 3, Line.Direction.VERTICAL)
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
            }

            else -> {}
        }
    }
}
