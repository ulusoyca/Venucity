package com.ulusoyapps.venucity.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.ulusoyapps.venucity.R

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView, imageUrl: String) {
    imageView.load(imageUrl) {
        crossfade(true)
        placeholder(R.drawable.ic_food)
    }
}
