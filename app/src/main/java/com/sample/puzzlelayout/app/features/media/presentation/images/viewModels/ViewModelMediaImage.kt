package com.sample.puzzlelayout.app.features.media.presentation.images.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.puzzlelayout.R
import com.sample.puzzlelayout.app.features.media.domain.images.entities.ItemMediaImageFolder
import com.sample.puzzlelayout.app.features.media.domain.images.useCases.UseCaseMediaImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModelMediaImage(private val useCaseMediaImage: UseCaseMediaImage) : ViewModel() {

    private val _foldersLiveData = MutableLiveData<List<ItemMediaImageFolder>>()
    val foldersLiveData: LiveData<List<ItemMediaImageFolder>> get() = _foldersLiveData

    private val _errorLiveData = MutableLiveData<Int>()
    val errorLiveData: LiveData<Int> get() = _errorLiveData

    init {
        getFolderNames()
    }

    private fun getFolderNames() = viewModelScope.launch(Dispatchers.IO) {
        useCaseMediaImage.getFolderNames()?.let { list ->
            if (list.size > 5) {
                delay(500)
            }
            _foldersLiveData.postValue(list)
        } ?: run {
            _errorLiveData.postValue(R.string.something_went_wrong)
        }
    }
}

