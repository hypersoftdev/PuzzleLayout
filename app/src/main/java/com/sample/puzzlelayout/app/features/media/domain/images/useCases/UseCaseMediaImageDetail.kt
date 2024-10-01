package com.sample.puzzlelayout.app.features.media.domain.images.useCases

import android.net.Uri
import com.sample.puzzlelayout.app.features.media.data.images.repository.RepositoryMediaImages
import com.sample.puzzlelayout.app.features.media.domain.images.entities.ItemMediaImagePhoto
import com.sample.puzzlelayout.utilities.utils.ConstantUtils

class UseCaseMediaImageDetail(private val repositoryMediaImages: RepositoryMediaImages) {

    /**
     * Sorted by Descending order
     */

    fun getImages(folderName: String): List<ItemMediaImagePhoto>? {
        val shouldGetAllImages = folderName.equals(ConstantUtils.GALLERY_ALL, true)
        return when (shouldGetAllImages) {
            true -> repositoryMediaImages.getAllImages()
            false -> repositoryMediaImages.getImages(folderName)
        }
    }

    fun doesUriExist(imageUri: Uri): Boolean? {
        return repositoryMediaImages.doesUriExist(imageUri)
    }
}