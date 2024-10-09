package com.hypersoft.puzzlelayouts.app.features.layouts.data.repository

import com.hypersoft.pzlayout.interfaces.PuzzleLayout
import com.hypersoft.puzzlelayouts.app.features.layouts.data.dataSource.PuzzleUtils

class RepoPuzzleUtils(private val puzzleLayouts: PuzzleUtils) {

    fun getPuzzleLayout(type: Int, borderSize: Int, themeId: Int): PuzzleLayout {
        return puzzleLayouts.getPuzzleLayout(type, borderSize, themeId)
    }

    fun getAllPuzzleLayouts(): List<PuzzleLayout> {
        return puzzleLayouts.getAllPuzzleLayouts()
    }

    fun getPuzzleLayouts(pieceCount: Int): List<PuzzleLayout> {
        return puzzleLayouts.getPuzzleLayouts(pieceCount)
    }

    fun isSlantLayout(puzzleLayout: PuzzleLayout): Boolean {
        return puzzleLayouts.isSlantLayout(puzzleLayout)
    }
}