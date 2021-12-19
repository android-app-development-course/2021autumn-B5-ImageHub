package com.hyosakura.imagehub.viewmodel

import android.annotation.SuppressLint
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
import kotlinx.coroutines.launch
import java.io.File

@SuppressLint("Range")
class DeviceImageViewModel(private val repository: DataRepository) : ViewModel() {
    lateinit var dirList: LiveData<List<DeviceDirEntity>>
    lateinit var imageList: LiveData<List<DeviceImageEntity>>

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

    fun deviceImageInDeviceDir(deviceDir: DeviceDirEntity): LiveData<List<DeviceImageEntity>> {
        viewModelScope.launch {
            val file = File(deviceDir.url!!).listFiles()
            val imageList = mutableListOf<DeviceImageEntity>()
            var id = 1
            file?.forEach {
                if (it.extension == "jpg" ||
                    it.extension == "png" ||
                    it.extension == "gif" ||
                    it.extension == "jpeg" ||
                    it.extension == "bmp"
                ) {
                    val bitmap = ImageUtil.decodeFile(it.absolutePath, 100)
                    val image = object : DeviceImageEntity(
                        imageId = id++,
                        name = it.name,
                        url = it.path,
                        ext = it.extension,
                        width = bitmap.width,
                        height = bitmap.height,
                        size = it.length().toDouble()
                    ) {}
                    imageList.add(image)
                }
            }
            flow<List<DeviceImageEntity>> {
                emit(imageList)
            }.asLiveData().also {
                this@DeviceImageViewModel.imageList = it
            }
        }
        return imageList
    }

    /**
     * 设备上包含图片的文件夹列表，包含该文件夹的一张图片
     */
    fun getDeviceImageFolderList(context: Context): LiveData<List<DeviceDirEntity>> {
        val cursor = getCursor(context)
        val folderList = mutableListOf<DeviceDirEntity>()
        val folderMap = mutableMapOf<File, Bitmap>()
        var id = 1
        cursor.use {
            while (cursor.moveToNext()) {
                val path = cursor.getString(
                    cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)
                )
                val folder = File(path).parentFile!!
                val folderName = folder.name
                if (folderMap.containsKey(folder)) {
                    continue
                }
                val bitmap = ImageUtil.decodeFile(path, 100)
                folderMap[folder] = bitmap
                val dir = object : DeviceDirEntity(id++, folderName, folder.path, bitmap) {}
                folderList.add(dir)
            }
        }
        return flow<List<DeviceDirEntity>> {
            emit(folderList)
        }.asLiveData().also {
            dirList = it
        }
    }

    fun importImage(image: DeviceImageEntity) {
        viewModelScope.launch {
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
            repository.insertImage(entity)
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