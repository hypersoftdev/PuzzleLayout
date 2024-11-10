package com.hypersoft.pzlayout.layouts.straight

import com.hypersoft.pzlayout.interfaces.PuzzleLayout

/**
 *   Developer: Abdul Rehman Hassan
 *   Date: 16/09/2024
 *   Profile:
 *     -> github.com/CelestialBeats
 *     -> linkedin.com/in/celestialbeats
 */

/**
 * A helper class for managing straight layouts for puzzles.
 * This class provides functionalities to create layouts based on the number of pieces.
 */
class StraightLayoutHelper private constructor() {

    companion object {
        /**
         * Generates a list of puzzle layouts based on the specified piece count.
         *
         * @param pieceCount The number of pieces in the puzzle.
         * @return A list of PuzzleLayout instances corresponding to the piece count.
         */
        fun getAllThemeLayout(pieceCount: Int): List<PuzzleLayout> {
            val puzzleLayouts = mutableListOf<PuzzleLayout>() // List to store the generated layouts

            // Create layouts based on the number of pieces
            when (pieceCount) {
                2 -> for (i in 0 until 6) {
                    puzzleLayouts.add(TwoStraightLayout(i)) // Add layouts for themes 0 to 5
                }

                3 -> for (i in 0 until 8) {
                    puzzleLayouts.add(ThreeStraightLayout(i)) // Add layouts for themes 0 to 7
                }

                4 -> for (i in 0 until 12) {
                    puzzleLayouts.add(FourStraightLayout(i)) // Add layouts for themes 0 to 11
                }

                5 -> for (i in 0 until 19) {
                    puzzleLayouts.add(FiveStraightLayout(i)) // Add layouts for themes 0 to 18
                }

                6 -> for (i in 0 until 12) {
                    puzzleLayouts.add(SixStraightLayout(i)) // Add layouts for themes 0 to 11
                }

                7 -> for (i in 0 until 9) {
                    puzzleLayouts.add(SevenStraightLayout(i)) // Add layouts for themes 0 to 8
                }

                8 -> for (i in 0 until 11) {
                    puzzleLayouts.add(EightStraightLayout(i)) // Add layouts for themes 0 to 10
                }

                9 -> for (i in 0 until 8) {
                    puzzleLayouts.add(NineStraightLayout(i)) // Add layouts for themes 0 to 7
                }
            }

            return puzzleLayouts // Return the list of generated layouts
        }
    }
}