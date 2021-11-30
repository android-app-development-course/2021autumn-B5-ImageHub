package com.hyosakura.imagehub.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory

object ImageUtil {
    private val options = BitmapFactory.Options()

    fun decodeFile(url: String, size: Int): Bitmap {
        options.inSampleSize = size
        return BitmapFactory.decodeFile(url, options)
    }
}