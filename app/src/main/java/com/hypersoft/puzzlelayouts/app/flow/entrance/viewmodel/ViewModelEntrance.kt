package com.hypersoft.puzzlelayouts.app.flow.entrance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModelEntrance : ViewModel() {

    private val _navigateLiveData = MutableLiveData<Boolean>()
    val navigateLiveData: LiveData<Boolean> get() = _navigateLiveData

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            delay(1500)
            _navigateLiveData.value = true
        }
    }
}