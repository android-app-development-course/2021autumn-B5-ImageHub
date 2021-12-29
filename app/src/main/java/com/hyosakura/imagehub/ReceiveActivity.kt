package com.hyosakura.imagehub

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.util.AppDatabase
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class ReceiveActivity : Activity() {
    private val scope = CoroutineScope(SupervisorJob())

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
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
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

    private fun getBitMapAndSave(uriList: List<Uri>) = scope.launch {
        Log.i("receive", uriList.toString())
        val option = BitmapFactory.Options()
        uriList.forEach {
            val bitmap = BitmapFactory.decodeStream(
                contentResolver.openInputStream(it),
                null,
                option
            )
            val fileName = it.path?.substringAfterLast("/") ?: "None"
            if (bitmap != null) {
                val path = ImageUtil.saveImageToLocalStorge(
                    this@ReceiveActivity.applicationContext,
                    bitmap,
                    fileName
                )
                val imageEntity = ImageEntity(
                    imageId = null,
                    folderId = -1,
                    name = fileName,
                    url = path,
                    ext = fileName.substringAfterLast('.', ""),
                    annotation = "",
                    bitmap.width,
                    bitmap.height,
                    bitmap.byteCount.toDouble(),
                    rating = null,
                    addTime = System.currentTimeMillis(),
                    shareTime = null,
                    deleted = 0
                )
                db.imageDao().insertImages(imageEntity)
            }
        }
    }
}