package com.hyosakura.imagehub.viewmodel

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.DeviceDirEntity
import com.hyosakura.imagehub.entity.DeviceImageEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.collections.set


@SuppressLint("Range")
class DeviceImageViewModel(private val repository: DataRepository) : ViewModel() {
    lateinit var dirList: LiveData<List<DeviceDirEntity>>
    private val map = mutableMapOf<Int, DeviceImageEntity>()
    val imageList: LiveData<List<DeviceImageEntity>>
        get() {
            return flow<List<DeviceImageEntity>> {
                map.values
            }.asLiveData()
        }

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

    fun getImageById(id: Int) = map[id]

    fun getDeviceImage(context: Context) {
        val cursor = context.contentResolver
            .query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATE_MODIFIED),
                null,
                null,
                "${MediaStore.MediaColumns.DATE_MODIFIED} desc"
            )!!
        val dirUrls = mutableListOf<String>()
        val folderMap = mutableMapOf<File, Bitmap>()
        cursor.use {
            val columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val imageId = cursor.getLong(columnIndexID)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId
                )
                val path = ImageUtil.getFilePathFromContentUri(uri, context.contentResolver)!!
                val folder = File(path).parentFile!!
                if (folderMap.containsKey(folder)) {
                    continue
                }
                dirUrls.add(folder.path)
            }
        }
        val imageList = mutableListOf<DeviceImageEntity>()
        dirUrls.forEach { dir ->
            val file = File(dir).listFiles()
            var id = 1
            file?.forEach {
                if (it.extension == "jpg" ||
                    it.extension == "png" ||
                    it.extension == "gif" ||
                    it.extension == "jpeg" ||
                    it.extension == "bmp"
                ) {
                    val bitmap = ImageUtil.decodeFile(it.absolutePath, 1)
                    val image = object : DeviceImageEntity(
                        imageId = id++,
                        name = it.name,
                        url = it.path,
                        ext = it.extension,
                        width = bitmap.width,
                        height = bitmap.height,
                        size = it.length().toDouble(),
                        bitmap = bitmap
                    ) {}
                    map[image.imageId!!] = image
                    imageList.add(image)
                }
            }
        }
    }

    suspend fun importImage(image: DeviceImageEntity): Int =
        withContext(viewModelScope.coroutineContext) {
            val entity = ImageEntity(
                imageId = null,
                name = image.name,
                url = image.url,
                ext = image.ext,
                width = image.width,
                height = image.height,
                size = image.size,
                addTime = System.currentTimeMillis()
            )
            repository.insertImage(entity).first().toInt()
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