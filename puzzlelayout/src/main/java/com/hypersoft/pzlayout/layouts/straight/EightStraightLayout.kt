package com.hypersoft.pzlayout.layouts.straight

import com.hypersoft.pzlayout.interfaces.Line

class EightStraightLayout(theme: Int) : NumberStraightLayout(theme) {

    override fun getThemeCount(): Int {
        return 11
    }

    override fun layout() {
        when (theme) {
            0 -> cutAreaEqualPart(0, 3, 1)
            1 -> cutAreaEqualPart(0, 1, 3)
            2 -> {
                cutAreaEqualPart(0, 4, Line.Direction.VERTICAL)
                addLine(3, Line.Direction.HORIZONTAL, 4f / 5)
                addLine(2, Line.Direction.HORIZONTAL, 3f / 5)
                addLine(1, Line.Direction.HORIZONTAL, 2f / 5)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 5)
            }

            3 -> {
                cutAreaEqualPart(0, 4, Line.Direction.HORIZONTAL)
                addLine(3, Line.Direction.VERTICAL, 4f / 5)
                addLine(2, Line.Direction.VERTICAL, 3f / 5)
                addLine(1, Line.Direction.VERTICAL, 2f / 5)
                addLine(0, Line.Direction.VERTICAL, 1f / 5)
            }

            4 -> {
                cutAreaEqualPart(0, 4, Line.Direction.VERTICAL)
                addLine(3, Line.Direction.HORIZONTAL, 1f / 5)
                addLine(2, Line.Direction.HORIZONTAL, 2f / 5)
                addLine(1, Line.Direction.HORIZONTAL, 3f / 5)
                addLine(0, Line.Direction.HORIZONTAL, 4f / 5)
            }

            5 -> {
                cutAreaEqualPart(0, 4, Line.Direction.HORIZONTAL)
                addLine(3, Line.Direction.VERTICAL, 1f / 5)
                addLine(2, Line.Direction.VERTICAL, 2f / 5)
                addLine(1, Line.Direction.VERTICAL, 3f / 5)
                addLine(0, Line.Direction.VERTICAL, 4f / 5)
            }

            6 -> {
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
                cutAreaEqualPart(2, 3, Line.Direction.VERTICAL)
                cutAreaEqualPart(1, 2, Line.Direction.VERTICAL)
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
            }

            7 -> {
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
                cutAreaEqualPart(2, 3, Line.Direction.HORIZONTAL)
                cutAreaEqualPart(1, 2, Line.Direction.HORIZONTAL)
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
            }

            8 -> {
                addLine(0, Line.Direction.HORIZONTAL, 4f / 5)
                cutAreaEqualPart(1, 5, Line.Direction.VERTICAL)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
                addLine(1, Line.Direction.VERTICAL, 1f / 2)
            }

            9 -> {
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
                cutAreaEqualPart(2, 2, Line.Direction.VERTICAL)
                cutAreaEqualPart(1, 3, Line.Direction.VERTICAL)
                addLine(0, Line.Direction.VERTICAL, 3f / 4)
                addLine(0, Line.Direction.VERTICAL, 1f / 3)
            }

            10 -> {
                cutAreaEqualPart(0, 2, 1)
                addLine(5, Line.Direction.VERTICAL, 1f / 2)
                addLine(4, Line.Direction.VERTICAL, 1f / 2)
            }

            else -> {}
        }
    }
}
