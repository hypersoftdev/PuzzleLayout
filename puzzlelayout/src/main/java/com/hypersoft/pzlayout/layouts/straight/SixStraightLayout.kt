package com.hypersoft.pzlayout.layouts.straight

import com.hypersoft.pzlayout.interfaces.Line

class SixStraightLayout(theme: Int) : NumberStraightLayout(theme) {

    override fun getThemeCount(): Int {
        return 12
    }

    override fun layout() {
        when (theme) {
            0 -> cutAreaEqualPart(0, 2, 1)
            1 -> cutAreaEqualPart(0, 1, 2)
            2 -> {
                addCross(0, 2f / 3, 1f / 2)
                addLine(3, Line.Direction.VERTICAL, 1f / 2)
                addLine(2, Line.Direction.VERTICAL, 1f / 2)
            }

            3 -> {
                addCross(0, 1f / 2, 2f / 3)
                addLine(3, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 2)
            }

            4 -> {
                addCross(0, 1f / 2, 1f / 3)
                addLine(2, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
            }

            5 -> {
                addCross(0, 1f / 3, 1f / 2)
                addLine(1, Line.Direction.VERTICAL, 1f / 2)
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
            }

            6 -> {
                addLine(0, Line.Direction.HORIZONTAL, 4f / 5)
                cutAreaEqualPart(1, 5, Line.Direction.VERTICAL)
            }

            7 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 4)
                addLine(1, Line.Direction.HORIZONTAL, 2f / 3)
                addLine(1, Line.Direction.VERTICAL, 1f / 4)
                addLine(2, Line.Direction.VERTICAL, 2f / 3)
                addLine(4, Line.Direction.VERTICAL, 1f / 2)
            }

            8 -> {
                addCross(0, 1f / 3)
                addLine(1, Line.Direction.VERTICAL, 1f / 2)
                addLine(4, Line.Direction.HORIZONTAL, 1f / 2)
            }

            9 -> {
                addCross(0, 2f / 3, 1f / 3)
                addLine(3, Line.Direction.VERTICAL, 1f / 2)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
            }

            10 -> {
                addCross(0, 2f / 3)
                addLine(2, Line.Direction.VERTICAL, 1f / 2)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 2)
            }

            11 -> {
                addCross(0, 1f / 3, 2f / 3)
                addLine(3, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(0, Line.Direction.VERTICAL, 1f / 2)
            }

            12 -> {
                addCross(0, 1f / 3)
                addLine(2, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(1, Line.Direction.VERTICAL, 1f / 2)
            }

            else -> {
                addCross(0, 2f / 3, 1f / 2)
                addLine(3, Line.Direction.VERTICAL, 1f / 2)
                addLine(2, Line.Direction.VERTICAL, 1f / 2)
            }
        }
    }
}
