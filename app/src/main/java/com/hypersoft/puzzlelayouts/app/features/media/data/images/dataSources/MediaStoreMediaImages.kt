package com.hypersoft.puzzlelayouts.app.features.media.data.images.dataSources

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.hypersoft.puzzlelayouts.app.features.media.domain.images.entities.ItemMediaImageFolder
import com.hypersoft.puzzlelayouts.app.features.media.domain.images.entities.ItemMediaImagePhoto
import com.hypersoft.puzzlelayouts.utilities.utils.ConstantUtils.TAG
import com.hypersoft.puzzlelayouts.utilities.utils.MediaStoreUtils
import java.io.FileNotFoundException

class MediaStoreMediaImages(private val contentResolver: ContentResolver?) {

    fun getFolderNames(): List<ItemMediaImageFolder>? {
        if (contentResolver == null) {
            Log.e(TAG, "getFolderNames: ", NullPointerException("ContentResolver is null"))
            return null
        }

        val mediaStoreUtils = MediaStoreUtils().MediaStoreFolders()
        val mutableSet = mutableSetOf<ItemMediaImageFolder>()

        contentResolver.query(
            mediaStoreUtils.contentUri,
            mediaStoreUtils.projection,
            mediaStoreUtils.filePathSelection,
            mediaStoreUtils.filePathSelectionArgs,
            mediaStoreUtils.sortBy
        )?.use { cursor ->
            val folderColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val folderName = cursor.getString(folderColumn) ?: "Unknown"
                mutableSet.add(ItemMediaImageFolder(folderName))
            }
        } ?: run {
            Log.e(TAG, "getFolderNames: ", NullPointerException("Cursor is null"))
            return null
        }
        return mutableSet.toList()
    }

    fun getAllImages(): List<ItemMediaImagePhoto>? {
        if (contentResolver == null) {
            Log.e(TAG, "getAllImages: ", NullPointerException("ContentResolver is null"))
            return null
        }

        val mediaStoreUtils = MediaStoreUtils().MediaStoreImages()
        val arrayList = ArrayList<ItemMediaImagePhoto>()

        contentResolver.query(
            mediaStoreUtils.contentUri,
            mediaStoreUtils.projection,
            mediaStoreUtils.filePathSelection,
            mediaStoreUtils.filePathSelectionArgs,
            mediaStoreUtils.sortBy
        )?.use { cursor ->
            val columnId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val imageUri: Uri = ContentUris.withAppendedId(mediaStoreUtils.contentUri, cursor.getLong(columnId))
                val photo = ItemMediaImagePhoto(uri = imageUri)
                arrayList.add(photo)
            }
            return arrayList
        } ?: kotlin.run {
            Log.e(TAG, "getAllImages: ", NullPointerException("Cursor is null"))
            return null
        }
    }

    fun getImages(folderName: String): List<ItemMediaImagePhoto>? {
        if (contentResolver == null) {
            Log.e(TAG, "getImages: ", NullPointerException("ContentResolver is null"))
            return null
        }

        val mediaStoreUtils = MediaStoreUtils().MediaStoreImagesByFolder(folderName)
        val arrayList = ArrayList<ItemMediaImagePhoto>()

        contentResolver.query(
            mediaStoreUtils.contentUri,
            mediaStoreUtils.projection,
            mediaStoreUtils.filePathSelection,
            mediaStoreUtils.filePathSelectionArgs,
            mediaStoreUtils.sortBy
        )?.use { cursor ->
            val columnId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val imageUri: Uri = ContentUris.withAppendedId(mediaStoreUtils.contentUri, cursor.getLong(columnId))
                val photo = ItemMediaImagePhoto(uri = imageUri)
                arrayList.add(photo)
            }
            return arrayList
        } ?: kotlin.run {
            Log.e(TAG, "getImages: FolderName($folderName): ", NullPointerException("Cursor is null"))
            return null
        }
    }

    fun doesUriExist(imageUri: Uri): Boolean? {
        if (contentResolver == null) {
            Log.e(TAG, "getImages: ", NullPointerException("ContentResolver is null"))
            return null
        }

        val mediaStoreUtils = MediaStoreUtils().MediaStoreUriExist(imageUri)

        // For Android 11 and above, check if the file is trashed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            contentResolver.query(
                mediaStoreUtils.contentUri,
                mediaStoreUtils.projection,
                mediaStoreUtils.filePathSelection,
                mediaStoreUtils.filePathSelectionArgs,
                mediaStoreUtils.sortBy
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val isTrashed = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.IS_TRASHED)) == 1
                    if (isTrashed) {
                        return false
                    }
                }
            } ?: run {
                return null
            }
        }
        try {
            contentResolver.openInputStream(imageUri)?.use {
                return true // File is accessible and exists
            } ?: run {
                return false
            }
        } catch (ex: FileNotFoundException) {
            return false
        }
    }
}