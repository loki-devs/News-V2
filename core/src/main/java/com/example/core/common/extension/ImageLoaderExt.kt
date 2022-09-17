package com.example.core.common.extension

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load

fun ImageView.loadImageUrl(imageUrl: String?, @DrawableRes placeholder: Int = -1) {
    imageUrl?.let {
        load(it) {
            allowHardware(false)
            if (placeholder != -1) placeholder(placeholder)
        }
    }
}