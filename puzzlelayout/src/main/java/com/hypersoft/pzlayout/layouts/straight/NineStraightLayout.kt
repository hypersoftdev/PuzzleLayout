package com.hypersoft.pzlayout.layouts.straight

import com.hypersoft.pzlayout.interfaces.Line

class NineStraightLayout(theme: Int) : NumberStraightLayout(theme) {

    override fun getThemeCount(): Int {
        return 8
    }

    override fun layout() {
        when (theme) {
            0 -> cutAreaEqualPart(0, 2, 2)
            1 -> {
                addLine(0, Line.Direction.VERTICAL, 3f / 4)
                addLine(0, Line.Direction.VERTICAL, 1f / 3)
                cutAreaEqualPart(2, 4, Line.Direction.HORIZONTAL)
                cutAreaEqualPart(0, 4, Line.Direction.HORIZONTAL)
            }

            2 -> {
                addLine(0, Line.Direction.HORIZONTAL, 3f / 4)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 3)
                cutAreaEqualPart(2, 4, Line.Direction.VERTICAL)
                cutAreaEqualPart(0, 4, Line.Direction.VERTICAL)
            }

            3 -> {
                addLine(0, Line.Direction.HORIZONTAL, 3f / 4)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 3)
                cutAreaEqualPart(2, 3, Line.Direction.VERTICAL)
                addLine(1, Line.Direction.VERTICAL, 3f / 4)
                addLine(1, Line.Direction.VERTICAL, 1f / 3)
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
            }

            4 -> {
                addLine(0, Line.Direction.VERTICAL, 3f / 4)
                addLine(0, Line.Direction.VERTICAL, 1f / 3)
                cutAreaEqualPart(2, 3, Line.Direction.HORIZONTAL)
                addLine(1, Line.Direction.HORIZONTAL, 3f / 4)
                addLine(1, Line.Direction.HORIZONTAL, 1f / 3)
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
            }

            5 -> {
                cutAreaEqualPart(0, 3, Line.Direction.VERTICAL)
                addLine(2, Line.Direction.HORIZONTAL, 3f / 4)
                addLine(2, Line.Direction.HORIZONTAL, 1f / 3)
                cutAreaEqualPart(1, 3, Line.Direction.HORIZONTAL)
                addLine(0, Line.Direction.HORIZONTAL, 3f / 4)
                addLine(0, Line.Direction.HORIZONTAL, 1f / 3)
            }

            6 -> {
                cutAreaEqualPart(0, 3, Line.Direction.HORIZONTAL)
                addLine(2, Line.Direction.VERTICAL, 3f / 4)
                addLine(2, Line.Direction.VERTICAL, 1f / 3)
                cutAreaEqualPart(1, 3, Line.Direction.VERTICAL)
                addLine(0, Line.Direction.VERTICAL, 3f / 4)
                addLine(0, Line.Direction.VERTICAL, 1f / 3)
            }

            7 -> {
                addLine(0, Line.Direction.HORIZONTAL, 1f / 2)
                cutAreaEqualPart(1, 1, 3)
            }

            else -> {}
        }
    }
}
