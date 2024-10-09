package com.hypersoft.puzzlelayouts.app.features.layouts.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hypersoft.pzlayout.interfaces.PuzzleLayout
import com.hypersoft.puzzlelayouts.app.features.layouts.domain.UseCasePuzzleLayouts
import com.hypersoft.puzzlelayouts.utilities.observers.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModelPuzzleLayouts(private val useCasePuzzleLayouts: UseCasePuzzleLayouts) : ViewModel() {

    private val _puzzleAllLayoutsLiveData = MutableLiveData<List<PuzzleLayout>>()
    val puzzleAllLayoutsLiveData: LiveData<List<PuzzleLayout>> get() = _puzzleAllLayoutsLiveData

    private val _puzzleLayoutsLiveData = MutableLiveData<List<PuzzleLayout>>()
    val puzzleLayoutsLiveData: LiveData<List<PuzzleLayout>> get() = _puzzleLayoutsLiveData

    private val _puzzleLayoutLiveData = MutableLiveData<PuzzleLayout>()
    val puzzleLayoutLiveData: LiveData<PuzzleLayout> get() = _puzzleLayoutLiveData

    private val _isSlantLiveData = SingleLiveEvent<Boolean>()
    val isSlantLiveData: LiveData<Boolean> get() = _isSlantLiveData


    init {
        getAllLayouts()
    }

    private fun getAllLayouts() = viewModelScope.launch(Dispatchers.IO) {
        useCasePuzzleLayouts.getAllAllPuzzleLayouts().let { list ->
            if (list.size > 5) {
                delay(500)
            }
            _puzzleAllLayoutsLiveData.postValue(list)
        }
    }

    fun getPuzzleLayouts(pieceCount: Int) = viewModelScope.launch(Dispatchers.IO) {
        useCasePuzzleLayouts.getPuzzleLayouts(pieceCount).let { list ->
            if (list.size > 5) {
                delay(500)
            }
            _puzzleLayoutsLiveData.postValue(list)
        }
    }

    fun getPuzzleLayout(type: Int, borderSize: Int, theme: Int) = viewModelScope.launch(Dispatchers.IO) {
        useCasePuzzleLayouts.getPuzzleLayout(type, borderSize, theme).let { list ->
            if (list.width() > 5) {
                delay(500)
            }
            _puzzleLayoutLiveData.postValue(list)
        }
    }

    fun layoutClick(puzzleLayout: PuzzleLayout) = viewModelScope.launch(Dispatchers.IO) {
        if (useCasePuzzleLayouts.isSlantLayout(puzzleLayout)) {
            _isSlantLiveData.postValue(true)
        } else {
            _isSlantLiveData.postValue(false)
        }
    }

}