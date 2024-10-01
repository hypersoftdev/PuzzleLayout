package com.puzzle.layouts.layouts.slant

import android.util.Log
import com.puzzle.layouts.slant.SlantPuzzleLayout

abstract class NumberSlantLayout(theme: Int) : SlantPuzzleLayout() {

    companion object {
        const val TAG = "NumberSlantLayout"
    }

    val theme: Int

    init {
        this.theme = if (theme >= safeGetThemeCount()) {
            Log.e(
                TAG, "NumberSlantLayout: the most theme count is " +
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

    // Remove the custom getTheme() method
    // You can access the theme directly as a property now
}


