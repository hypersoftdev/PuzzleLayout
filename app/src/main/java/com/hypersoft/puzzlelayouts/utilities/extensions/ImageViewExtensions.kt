package com.hypersoft.puzzlelayouts.utilities.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 *   Developer: Sohaib Ahmed
 *   Date: 9/15/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

fun ImageView.setImageFromDrawableRes(@DrawableRes drawableRes: Int) {
    Glide.with(this)
        .load(drawableRes)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .into(this)
}

fun ImageView.setImageFromUri(imageUri: Uri) {
    Glide.with(this)
        .load(imageUri)
        .transition(DrawableTransitionOptions.withCrossFade(300))
        .into(this)
}

fun ImageView.setImageFromUriPath(imageUriPath: String) {
    Glide.with(this)
        .load(imageUriPath)
        .into(this)
}