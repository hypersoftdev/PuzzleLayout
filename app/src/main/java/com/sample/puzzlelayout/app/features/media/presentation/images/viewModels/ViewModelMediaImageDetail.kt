package com.sample.puzzlelayout.app.features.media.presentation.images.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.puzzlelayout.R
import com.sample.puzzlelayout.app.features.media.domain.images.entities.ItemMediaImagePhoto
import com.sample.puzzlelayout.app.features.media.domain.images.useCases.UseCaseMediaImageDetail
import com.sample.puzzlelayout.utilities.observers.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelMediaImageDetail(private val useCaseMediaImageDetail: UseCaseMediaImageDetail) : ViewModel() {

    private val _imagesLiveData = MutableLiveData<List<ItemMediaImagePhoto>>()
    val imagesLiveData: LiveData<List<ItemMediaImagePhoto>> get() = _imagesLiveData

    private val _refreshLiveData = SingleLiveEvent<Boolean>()
    val refreshLiveData: LiveData<Boolean> get() = _refreshLiveData

    private val _errorLiveData = SingleLiveEvent<Int>()
    val errorLiveData: LiveData<Int> get() = _errorLiveData

    private val _clickedImagesLiveData = MutableLiveData<List<ItemMediaImagePhoto>>()
    val clickedImagesLiveData: LiveData<List<ItemMediaImagePhoto>> get() = _clickedImagesLiveData

    private val clickedImages = mutableListOf<ItemMediaImagePhoto>()

    fun getImages(folderName: String) = viewModelScope.launch(Dispatchers.IO) {
        useCaseMediaImageDetail.getImages(folderName)?.let { list ->
            _imagesLiveData.postValue(list)
        } ?: run {
            _errorLiveData.postValue(R.string.something_went_wrong)
        }
    }

    fun imageClick(imageUri: Uri) = viewModelScope.launch(Dispatchers.IO) {
        useCaseMediaImageDetail.doesUriExist(imageUri)?.let { isExist ->
            if (isExist) {
                val item = ItemMediaImagePhoto(imageUri)

                // Check if the image already exists in the list
                if (clickedImages.contains(item)) {
                    // Remove the image if it exists
                    val updatedList = clickedImages.toMutableList()
                    updatedList.remove(item)
                    _clickedImagesLiveData.postValue(updatedList)

                    // Update the internal list
                    clickedImages.clear()
                    clickedImages.addAll(updatedList)
                } else {
                    // Add the image if it doesn't exist
                    val updatedList = clickedImages.toMutableList()
                    updatedList.add(item)
                    _clickedImagesLiveData.postValue(updatedList)

                    // Update the internal list
                    clickedImages.clear()
                    clickedImages.addAll(updatedList)
                }

                return@launch
            }
            _errorLiveData.postValue(R.string.image_not_found)
            _refreshLiveData.postValue(true)
        } ?: run {
            _errorLiveData.postValue(R.string.something_went_wrong)
        }
    }

}