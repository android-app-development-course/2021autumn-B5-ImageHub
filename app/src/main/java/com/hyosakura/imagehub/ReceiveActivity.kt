package com.hyosakura.imagehub

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.util.AppDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class ReceiveActivity : AppCompatActivity() {
    private val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val action = intent.action
        val type = intent.type
        if (Intent.ACTION_SEND == action && type != null) {
            if (type.startsWith("image/")) {
                dealPicStream(intent)
            }
        } else if (Intent.ACTION_SEND_MULTIPLE == action && type != null) {
            if (type.startsWith("image/")) {
                dealMultiplePicStream(intent)
            }
        }
    }

    private fun dealPicStream(intent: Intent) {
        val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        if (uri != null) {
            getBitMapAndSave(listOf(uri))
        }
    }

    private fun dealMultiplePicStream(intent: Intent) {
        val uriList = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
        if (uriList != null) {
            getBitMapAndSave(uriList)
        }
    }

    private fun getBitMapAndSave(uriList: List<Uri>) {
        val option = BitmapFactory.Options()
        uriList.forEach {
            val bitmap = BitmapFactory.decodeStream(
                contentResolver.openInputStream(it),
                null,
                option
            )
            if (bitmap != null) {
                val file = it.toFile()
                val path = saveImage(
                    bitmap,
                    file.name
                )
                val imageEntity = ImageEntity(
                    imageId = null,
                    dirId = -1,
                    name = file.name,
                    url = path,
                    ext = it.toFile().extension,
                    annotation = null,
                    bitmap.width,
                    bitmap.height,
                    FileInputStream(file).available().toDouble(),
                    rating = null,
                    addTime = System.currentTimeMillis(),
                    shareTime = null,
                    deleted = 0
                )
                db.imageDao().insertImages(imageEntity)
            }
        }
    }

    private fun saveImage(
        bitmap: Bitmap,
        filename: String,
    ): String? {
        val f = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename
        )
        try {
            val fos = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return f.absolutePath
    }
}