package com.sample.puzzlelayout.app.features.layouts.domain

import com.puzzle.layouts.interfaces.PuzzleLayout
import com.sample.puzzlelayout.app.features.layouts.data.repository.RepoPuzzleUtils

class UseCasePuzzleLayouts(private val repoPuzzleUtils: RepoPuzzleUtils) {

    fun getAllAllPuzzleLayouts(): List<PuzzleLayout> {
        return repoPuzzleUtils.getAllPuzzleLayouts()
    }

    fun getPuzzleLayouts(pieceCount: Int): List<PuzzleLayout> {
        return repoPuzzleUtils.getPuzzleLayouts(pieceCount)
    }

    fun getPuzzleLayout(type: Int, borderSize: Int, theme: Int): PuzzleLayout {
        return repoPuzzleUtils.getPuzzleLayout(type, borderSize, theme)
    }

    fun isSlantLayout(puzzleLayout: PuzzleLayout): Boolean {
        return repoPuzzleUtils.isSlantLayout(puzzleLayout)
    }
}