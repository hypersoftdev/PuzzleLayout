package com.puzzle.layouts.layouts.slant

import com.puzzle.layouts.interfaces.Line

class TwoSlantLayout(theme: Int) : NumberSlantLayout(theme) {

    override fun getThemeCount(): Int {
        return 3
    }

    override fun layout() {
        when (theme) {
            0 -> addLine(0, Line.Direction.VERTICAL, 0.56f, 0.44f)
            1 -> addLine(0, Line.Direction.VERTICAL, 0.8f, 0.2f)
            2 -> addLine(0, Line.Direction.VERTICAL, 0.2f, 0.8f)
        }
    }
}
