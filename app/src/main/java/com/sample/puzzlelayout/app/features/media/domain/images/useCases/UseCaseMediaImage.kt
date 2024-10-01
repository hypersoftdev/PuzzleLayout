package com.sample.puzzlelayout.app.features.media.domain.images.useCases

import com.sample.puzzlelayout.app.features.media.data.images.repository.RepositoryMediaImages
import com.sample.puzzlelayout.app.features.media.domain.images.entities.ItemMediaImageFolder

class UseCaseMediaImage(private val repositoryMediaImages: RepositoryMediaImages) {

    /**
     * Sorted by Ascending order
     */

    fun getFolderNames(): List<ItemMediaImageFolder>? {
        return repositoryMediaImages.getFolderNames()
    }
}