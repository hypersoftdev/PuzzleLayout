package com.puzzle.layouts.layouts.slant

import com.puzzle.layouts.interfaces.Line

class ThreeSlantLayout(theme: Int) : NumberSlantLayout(theme) {

    override fun getThemeCount(): Int {
        return 9 // Updated to include additional themes
    }

    override fun layout() {
        when (theme) {
            0 -> {
                addLine(0, Line.Direction.HORIZONTAL, 0.5f)
                addLine(0, Line.Direction.VERTICAL, 0.56f, 0.44f)
            }

            1 -> {
                addLine(0, Line.Direction.HORIZONTAL, 0.5f)
                addLine(1, Line.Direction.VERTICAL, 0.56f, 0.44f)
            }

            2 -> {
                addLine(0, Line.Direction.VERTICAL, 0.5f)
                addLine(0, Line.Direction.HORIZONTAL, 0.56f, 0.44f)
            }

            3 -> {
                addLine(0, Line.Direction.VERTICAL, 0.5f)
                addLine(1, Line.Direction.HORIZONTAL, 0.56f, 0.44f)
            }

            4 -> {
                addLine(0, Line.Direction.HORIZONTAL, 0.44f, 0.56f)
                addLine(0, Line.Direction.VERTICAL, 0.56f, 0.44f)
            }

            5 -> {
                addLine(0, Line.Direction.VERTICAL, 0.56f, 0.44f)
                addLine(1, Line.Direction.HORIZONTAL, 0.44f, 0.56f)
            }

            6 -> {
                addLine(0, Line.Direction.HORIZONTAL, 0.3f)
                addLine(0, Line.Direction.VERTICAL, 0.7f, 0.3f)
            }

            7 -> {
                addLine(0, Line.Direction.VERTICAL, 0.3f)
                addLine(0, Line.Direction.HORIZONTAL, 0.7f, 0.3f)
            }

            8 -> {
                addLine(0, Line.Direction.VERTICAL, 0.3f)
                addLine(1, Line.Direction.HORIZONTAL, 0.7f, 0.3f)
            }
        }
    }
}

