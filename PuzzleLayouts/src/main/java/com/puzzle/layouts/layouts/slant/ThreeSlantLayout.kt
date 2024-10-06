package com.puzzle.layouts.layouts.slant

import com.puzzle.layouts.interfaces.Line

class ThreeSlantLayout(theme: Int) : NumberSlantLayout(theme) {

    override fun getThemeCount(): Int {
        return 3 // Updated to include additional themes
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
                addLine(0, Line.Direction.HORIZONTAL, 0.3f)
                addLine(0, Line.Direction.VERTICAL, 0.7f, 0.3f)
            }
        }
    }
}

