package com.hypersoft.puzzlelayouts.app.features.layouts.data.dataSource

import com.hypersoft.pzlayout.interfaces.PuzzleLayout
import com.hypersoft.pzlayout.layouts.slant.SlantLayoutHelper
import com.hypersoft.pzlayout.layouts.slant.ThreeSlantLayout
import com.hypersoft.pzlayout.layouts.slant.TwoSlantLayout
import com.hypersoft.pzlayout.layouts.straight.FiveStraightLayout
import com.hypersoft.pzlayout.layouts.straight.FourStraightLayout
import com.hypersoft.pzlayout.layouts.straight.StraightLayoutHelper
import com.hypersoft.pzlayout.layouts.straight.ThreeStraightLayout
import com.hypersoft.pzlayout.layouts.straight.TwoStraightLayout
import com.hypersoft.pzlayout.slant.SlantPuzzleLayout

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
            addAll(SlantLayoutHelper.getAllThemeLayout(3))

            addAll(StraightLayoutHelper.getAllThemeLayout(2))
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
