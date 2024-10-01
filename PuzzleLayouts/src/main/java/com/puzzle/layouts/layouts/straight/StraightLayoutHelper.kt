package com.puzzle.layouts.layouts.straight

import com.puzzle.layouts.interfaces.PuzzleLayout

class StraightLayoutHelper private constructor() {

    companion object {
        fun getAllThemeLayout(pieceCount: Int): List<PuzzleLayout> {
            val puzzleLayouts = mutableListOf<PuzzleLayout>()
            when (pieceCount) {
                2 -> for (i in 0 until 6) {
                    puzzleLayouts.add(TwoStraightLayout(i))
                }

                3 -> for (i in 0 until 8) {
                    puzzleLayouts.add(ThreeStraightLayout(i))
                }

                4 -> for (i in 0 until 12) {
                    puzzleLayouts.add(FourStraightLayout(i))
                }

                5 -> for (i in 0 until 19) {
                    puzzleLayouts.add(FiveStraightLayout(i))
                }
            }

            return puzzleLayouts
        }
    }
}
