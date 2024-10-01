package com.sample.puzzlelayout.utilities.utils

import android.net.Uri
import android.provider.MediaStore

class MediaStoreUtils {

    inner class MediaStoreFolders {
        val contentUri: Uri by lazy { MediaStore.Images.Media.EXTERNAL_CONTENT_URI }
        val projection by lazy { arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME) }
        val filePathSelection: String? by lazy { null }
        val filePathSelectionArgs: Array<String>? by lazy { null }
        val sortBy by lazy { MediaStore.Images.Media.BUCKET_DISPLAY_NAME }
    }

    inner class MediaStoreImages {
        val contentUri: Uri by lazy { MediaStore.Images.Media.EXTERNAL_CONTENT_URI }
        val projection by lazy { arrayOf(MediaStore.Images.Media._ID) }
        val filePathSelection: String? by lazy { null }
        val filePathSelectionArgs: Array<String>? by lazy { null }
        val sortBy by lazy { MediaStore.Images.Media.DATE_MODIFIED + " DESC" }
    }

    inner class MediaStoreImagesByFolder(folderName: String) {
        val contentUri: Uri by lazy { MediaStore.Images.Media.EXTERNAL_CONTENT_URI }
        val projection by lazy { arrayOf(MediaStore.Images.Media._ID) }
        val filePathSelection by lazy { MediaStore.Images.Media.DATA + " LIKE ?" }
        val filePathSelectionArgs by lazy { arrayOf("%/$folderName/%") }
        val sortBy by lazy { MediaStore.Images.Media.DATE_MODIFIED + " DESC" }
    }

    inner class MediaStoreUriExist(uri: Uri) {
        val contentUri: Uri by lazy { uri }
        val projection by lazy { arrayOf(MediaStore.Images.Media.IS_TRASHED) }
        val filePathSelection: String? by lazy { null }
        val filePathSelectionArgs: Array<String>? by lazy { null }
        val sortBy: String? by lazy { null }
    }

    inner class MediaStoreImageDetail {
        val projection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,    // fileName
            MediaStore.Images.Media.SIZE,            // fileSize
            MediaStore.Images.Media.DATA,            // filePath
            MediaStore.Images.Media.WIDTH,           // resolution width
            MediaStore.Images.Media.HEIGHT,          // resolution height
            MediaStore.Images.Media.DATE_MODIFIED    // lastModified
        )
    }
}