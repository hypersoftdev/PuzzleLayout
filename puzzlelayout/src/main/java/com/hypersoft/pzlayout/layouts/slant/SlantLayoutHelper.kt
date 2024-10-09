package com.hypersoft.pzlayout.layouts.slant

import com.hypersoft.pzlayout.interfaces.PuzzleLayout

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

/**
 * Helper object for managing slant layouts for puzzles.
 * Provides functionalities to create layouts based on the number of pieces.
 */
object SlantLayoutHelper {

    /**
     * Generates a list of puzzle layouts based on the specified piece count.
     *
     * @param pieceCount The number of pieces in the puzzle.
     * @return A list of PuzzleLayout instances corresponding to the piece count.
     */
    fun getAllThemeLayout(pieceCount: Int): List<PuzzleLayout> {
        val puzzleLayouts = mutableListOf<PuzzleLayout>() // List to store the generated layouts
        when (pieceCount) {
            2 -> {
                // Create and add layouts for a puzzle with 2 pieces
                for (i in 0 until 3) {
                    puzzleLayouts.add(TwoSlantLayout(i)) // Add layouts for themes 0 to 3
                }
            }

            3 -> {
                // Create and add layouts for a puzzle with 3 pieces
                for (i in 0 until 3) {
                    puzzleLayouts.add(ThreeSlantLayout(i)) // Add layouts for themes 0 to 7
                }
            }
        }
        return puzzleLayouts // Return the list of generated layouts
    }
}
