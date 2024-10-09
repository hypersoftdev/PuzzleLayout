package com.hypersoft.puzzlelayouts.app.features.media.presentation.images.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hypersoft.puzzlelayouts.app.features.media.domain.images.useCases.UseCaseMediaImageDetail

class ViewModelMediaImageDetailProvider(private val useCaseMediaImageDetail: UseCaseMediaImageDetail) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelMediaImageDetail::class.java)) {
            return ViewModelMediaImageDetail(useCaseMediaImageDetail) as T
        }
        return super.create(modelClass)
    }
}