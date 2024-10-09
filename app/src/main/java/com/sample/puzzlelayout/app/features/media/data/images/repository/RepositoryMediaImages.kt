package com.sample.puzzlelayout.app.features.media.data.images.repository

import android.net.Uri
import com.sample.puzzlelayout.app.features.media.domain.images.entities.ItemMediaImageFolder
import com.sample.puzzlelayout.app.features.media.domain.images.entities.ItemMediaImagePhoto
import com.sample.puzzlelayout.app.features.media.data.images.dataSources.MediaStoreMediaImages

class RepositoryMediaImages(private val mediaStoreEnhanceGallery: MediaStoreMediaImages) {

    fun getFolderNames(): List<ItemMediaImageFolder>? {
        return mediaStoreEnhanceGallery.getFolderNames()
    }

    fun getAllImages(): List<ItemMediaImagePhoto>? {
        return mediaStoreEnhanceGallery.getAllImages()
    }

    fun getImages(folderName: String): List<ItemMediaImagePhoto>? {
        return mediaStoreEnhanceGallery.getImages(folderName)
    }

    fun doesUriExist(imageUri: Uri): Boolean? {
        return mediaStoreEnhanceGallery.doesUriExist(imageUri)
    }
}