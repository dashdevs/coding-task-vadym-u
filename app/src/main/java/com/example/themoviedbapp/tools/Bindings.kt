package com.example.themoviedbapp.tools

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object Bindings {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImageUrl(view: ImageView, url: String?) {
        url.takeUnless { it.isNullOrEmpty() }?.let {
            Glide.with(view).load(it).placeholder(
                ColorDrawable(
                    ContextCompat.getColor(view.context, android.R.color.darker_gray)
                )
            ).into(view)
        }
    }
}