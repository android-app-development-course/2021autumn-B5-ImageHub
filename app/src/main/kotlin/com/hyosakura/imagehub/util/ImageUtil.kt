package com.hyosakura.imagehub.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

object ImageUtil {
    private val options = BitmapFactory.Options()

    fun decodeFile(url: String, size: Int): Bitmap {
        options.inSampleSize = size
        return BitmapFactory.decodeFile(url, options)
    }

    fun Context.share(uriStr: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        val uri = Uri.parse(uriStr)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        this.startActivity(Intent.createChooser(intent, "来自ImageHub的分享"))
    }
}