package com.sample.puzzlelayout.app.features.layouts.data.dataSource

import com.puzzle.layouts.interfaces.PuzzleLayout
import com.puzzle.layouts.layouts.slant.SlantLayoutHelper
import com.puzzle.layouts.layouts.slant.ThreeSlantLayout
import com.puzzle.layouts.layouts.slant.TwoSlantLayout
import com.puzzle.layouts.layouts.straight.FiveStraightLayout
import com.puzzle.layouts.layouts.straight.FourStraightLayout
import com.puzzle.layouts.layouts.straight.StraightLayoutHelper
import com.puzzle.layouts.layouts.straight.ThreeStraightLayout
import com.puzzle.layouts.layouts.straight.TwoStraightLayout
import com.puzzle.layouts.slant.SlantPuzzleLayout

class PuzzleUtils {

    fun getPuzzleLayout(type: Int, borderSize: Int, themeId: Int): PuzzleLayout {
        return if (type == 0) {
            when (borderSize) {
                2 -> TwoSlantLayout(themeId)
                3 -> ThreeSlantLayout(themeId)
                else -> TwoSlantLayout(themeId)
            }
        } else {
            when (borderSize) {
                2 -> TwoStraightLayout(themeId)
                3 -> ThreeStraightLayout(themeId)
                4 -> FourStraightLayout(themeId)
                5 -> FiveStraightLayout(themeId)
                else -> TwoStraightLayout(themeId)
            }
        }
    }

    fun getAllPuzzleLayouts(): List<PuzzleLayout> {
        return mutableListOf<PuzzleLayout>().apply {
            addAll(SlantLayoutHelper.getAllThemeLayout(2))
            addAll(StraightLayoutHelper.getAllThemeLayout(2))
            addAll(SlantLayoutHelper.getAllThemeLayout(3))
            addAll(StraightLayoutHelper.getAllThemeLayout(3))
            addAll(StraightLayoutHelper.getAllThemeLayout(4))
            addAll(StraightLayoutHelper.getAllThemeLayout(5))
        }
    }

    fun getPuzzleLayouts(pieceCount: Int): List<PuzzleLayout> {
        return mutableListOf<PuzzleLayout>().apply {
            addAll(SlantLayoutHelper.getAllThemeLayout(pieceCount))
            addAll(StraightLayoutHelper.getAllThemeLayout(pieceCount))
        }
    }

    fun isSlantLayout(puzzleLayout: PuzzleLayout): Boolean {
        return puzzleLayout is SlantPuzzleLayout
    }
}
