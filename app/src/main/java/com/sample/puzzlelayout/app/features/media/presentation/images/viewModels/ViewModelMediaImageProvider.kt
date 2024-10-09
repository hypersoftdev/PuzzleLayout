package com.sample.puzzlelayout.app.features.media.presentation.images.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.puzzlelayout.app.features.media.domain.images.useCases.UseCaseMediaImage

class ViewModelMediaImageProvider(private val useCaseMediaImage: UseCaseMediaImage) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelMediaImage::class.java)) {
            return ViewModelMediaImage(useCaseMediaImage) as T
        }
        return super.create(modelClass)
    }
}