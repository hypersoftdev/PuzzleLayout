package com.hypersoft.pzlayout.layouts.straight

import android.util.Log
import com.hypersoft.pzlayout.layouts.slant.NumberSlantLayout
import com.hypersoft.pzlayout.straight.StraightPuzzleLayout

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

/**
 * Abstract class representing a straight layout for number puzzles.
 * Inherits from StraightPuzzleLayout and provides theme-related functionalities.
 *
 * @param theme The theme index for the layout. Should be in the range of available themes.
 */
abstract class NumberStraightLayout(theme: Int) : StraightPuzzleLayout() {

    companion object {
        const val TAG = "NumberStraightLayout" // Tag for logging purposes
    }

    val theme: Int // Holds the validated theme index

    init {
        // Validate the provided theme index and log an error if it's out of bounds
        this.theme = if (theme >= safeGetThemeCount()) {
            Log.e(
                NumberSlantLayout.TAG, "NumberStraightLayout: the most theme count is " +
                        safeGetThemeCount() +
                        " ,you should let theme from 0 to " +
                        (safeGetThemeCount() - 1) + " ."
            )
            // If the theme index is out of bounds, default to the last available theme
            safeGetThemeCount() - 1
        } else {
            theme // Use the provided theme index if valid
        }
    }

    /**
     * Safely retrieves the count of available themes.
     * This function wraps the call to getThemeCount().
     *
     * @return The number of available themes.
     */
    private fun safeGetThemeCount(): Int {
        return getThemeCount()
    }

    /**
     * Abstract method to get the total count of themes available.
     * Must be implemented by subclasses to provide the correct theme count.
     *
     * @return The total number of themes.
     */
    abstract fun getThemeCount(): Int
}
