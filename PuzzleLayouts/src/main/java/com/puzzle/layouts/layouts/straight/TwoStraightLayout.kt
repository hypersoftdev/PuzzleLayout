package com.puzzle.layouts.layouts.straight

import android.util.Log
import com.puzzle.layouts.interfaces.Line

class TwoStraightLayout : NumberStraightLayout {

    private var mRadio: Float = 1f / 2

    constructor(theme: Int) : super(theme)

    constructor(radio: Float, theme: Int) : super(theme) {
        if (radio > 1) {
            Log.e(TAG, "TwoStraightLayout: the radio cannot be greater than 1f")
            mRadio = 1f
        } else {
            mRadio = radio
        }
    }

    override fun getThemeCount(): Int {
        return 6
    }

    override fun layout() {
        when (theme) {
            0 -> addLine(0, Line.Direction.HORIZONTAL, mRadio)
            1 -> addLine(0, Line.Direction.VERTICAL, mRadio)
            2 -> addLine(0, Line.Direction.HORIZONTAL, 1f / 3)
            3 -> addLine(0, Line.Direction.HORIZONTAL, 2f / 3)
            4 -> addLine(0, Line.Direction.VERTICAL, 1f / 3)
            5 -> addLine(0, Line.Direction.VERTICAL, 2f / 3)
            else -> addLine(0, Line.Direction.HORIZONTAL, mRadio)
        }
    }
}
