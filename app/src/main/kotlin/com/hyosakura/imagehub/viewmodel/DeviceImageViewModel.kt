package com.hyosakura.imagehub.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import java.io.File


class DeviceImageViewModel(private val repository: DataRepository) : ViewModel() {
    private fun getCursor(context: Context): Cursor {
        val resolver = context.contentResolver
        return resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
            arrayOf("image/jpeg", "image/png"),
            MediaStore.Images.Media.DATE_MODIFIED
        )!!
    }

    /**
     * 设备上包含图片的文件夹列表，包含该文件夹的一张图片
     */
    @SuppressLint("Range")
    fun getDeviceImageFolderList(context: Context, size: Int): List<Pair<String, Bitmap>> {
        val cursor = getCursor(context)
        val folderList = mutableListOf<Pair<String, Bitmap>>()
        val folderMap = mutableMapOf<String, Bitmap>()
        cursor.use {
            while (cursor.moveToNext()) {
                val path =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))
                val folder = File(path).parentFile!!
                val folderName = folder.name
                if (folderMap.containsKey(folderName)) {
                    continue
                }
                val bitmap = ImageUtil.decodeFile(path, size)
                folderMap[folderName] = bitmap
                folderList.add(Pair(folderName, bitmap))
            }
        }
        return folderList
    }
}

class DeviceImageViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeviceImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeviceImageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}