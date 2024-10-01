package com.puzzle.layouts.layouts.straight

import android.util.Log
import com.puzzle.layouts.layouts.slant.NumberSlantLayout
import com.puzzle.layouts.straight.StraightPuzzleLayout

abstract class NumberStraightLayout(theme: Int) : StraightPuzzleLayout() {

    companion object {
        const val TAG = "NumberStraightLayout"
    }

    val theme: Int

    init {
        this.theme = if (theme >= safeGetThemeCount()) {
            Log.e(
                NumberSlantLayout.TAG, "NumberStraightLayout: the most theme count is " +
                        safeGetThemeCount() +
                        " ,you should let theme from 0 to " +
                        (safeGetThemeCount() - 1) + " ."
            )
            // Provide a default valid theme if the provided one is invalid
            safeGetThemeCount() - 1
        } else {
            theme
        }
    }

    // Private final method that calls getThemeCount() after initialization is complete
    private fun safeGetThemeCount(): Int {
        return getThemeCount()
    }

    abstract fun getThemeCount(): Int

}

