package com.sample.puzzlelayout.app.features.media.domain.images.entities

import android.net.Uri


data class ItemMediaImagePhoto(
    val uri: Uri,
    var selected: Boolean = false
)