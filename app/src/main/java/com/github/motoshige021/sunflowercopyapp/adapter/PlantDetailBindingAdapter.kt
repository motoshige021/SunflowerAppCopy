package com.github.motoshige021.sunflowercopyapp.adapter

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        view.setImageURI(Uri.parse(imageUrl))
    }
}