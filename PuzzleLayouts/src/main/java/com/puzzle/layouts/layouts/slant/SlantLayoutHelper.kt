package com.puzzle.layouts.layouts.slant

import com.puzzle.layouts.interfaces.PuzzleLayout

object SlantLayoutHelper {

    fun getAllThemeLayout(pieceCount: Int): List<PuzzleLayout> {
        val puzzleLayouts = mutableListOf<PuzzleLayout>()
        when (pieceCount) {
            2 -> {
                for (i in 0 until 4) {
                    puzzleLayouts.add(TwoSlantLayout(i))
                }
            }

            3 -> {
                for (i in 0 until 8) {
                    puzzleLayouts.add(ThreeSlantLayout(i))
                }
            }
        }
        return puzzleLayouts
    }
}
