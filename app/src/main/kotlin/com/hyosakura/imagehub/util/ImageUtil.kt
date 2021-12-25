package com.hyosakura.imagehub.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns


object ImageUtil {
    private val options = BitmapFactory.Options()

    fun decodeFile(url: String, size: Int): Bitmap {
        options.inSampleSize = size
        return BitmapFactory.decodeFile(url, options)
    }

    fun Context.share(bitmap: Bitmap) {
        val intent =  Intent()
        intent.action = Intent.ACTION_SEND
        val values =  ContentValues()
        val cr = this.contentResolver
        values.put(MediaStore.Images.Media.TITLE, "IMG:${System.currentTimeMillis()}")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val imageOut = cr.openOutputStream(url!!)
        imageOut.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, url)
        this.startActivity(Intent.createChooser(intent, "来自ImageHub的分享"))
    }

    fun getFilePathFromContentUri(
        selectedVideoUri: Uri?,
        contentResolver: ContentResolver
    ): String? {
        val filePath: String
        val filePathColumn = arrayOf(MediaColumns.DATA)
        val cursor = contentResolver.query(selectedVideoUri!!, filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        filePath = cursor.getString(columnIndex)
        cursor.close()
        return filePath
    }
}