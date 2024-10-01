package com.puzzle.layouts.layouts.slant

import com.puzzle.layouts.interfaces.Line

class TwoSlantLayout(theme: Int) : NumberSlantLayout(theme) {

    override fun getThemeCount(): Int {
        return 4
    }

    override fun layout() {
        when (theme) {
            0 -> addLine(0, Line.Direction.HORIZONTAL, 0.56f, 0.44f)
            1 -> addLine(0, Line.Direction.VERTICAL, 0.56f, 0.44f)
            2 -> addLine(0, Line.Direction.VERTICAL, 0.8f, 0.2f)
            3 -> addLine(0, Line.Direction.VERTICAL, 0.2f, 0.8f)
//            4 -> addZigzagLine(0, Line.Direction.VERTICAL, 0.5f, 0.5f,5,0.1f)
        }
    }
}
