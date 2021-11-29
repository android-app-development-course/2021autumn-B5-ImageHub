package com.hyosakura.imagehub.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.hyosakura.imagehub.repository.DataRepository
import kotlinx.coroutines.flow.flow
import java.io.File


class DeviceImageViewModel(private val repository: DataRepository) : ViewModel() {
    private val options = BitmapFactory.Options()

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
     * 指定设备上文件夹的最近修改图片
     */
    @SuppressLint("Range")
    fun getDeviceImage(context: Context, size: Int): LiveData<List<Bitmap>> {
        return getCursor(context).use {
            options.inSampleSize = size
            val list = mutableListOf<Bitmap>()
            while (it.moveToNext()) {
                val path =
                    it.getString(it.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))
                list.add(BitmapFactory.decodeFile(path, options))
            }
            flow<List<Bitmap>> {
                emit(list)
            }.asLiveData()
        }
    }

    /**
     * 设备上包含图片的文件夹列表
     */
    @SuppressLint("Range")
    fun dirContainImage(context: Context): LiveData<List<File>> {
        return getCursor(context).use {
            val list = mutableListOf<File>()
            while (it.moveToNext()) {
                val path =
                    it.getString(it.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))
                val dir = File(path).parentFile!!
                list.add(dir)
            }
            flow<List<File>> {
                emit(list)
            }.asLiveData()
        }
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