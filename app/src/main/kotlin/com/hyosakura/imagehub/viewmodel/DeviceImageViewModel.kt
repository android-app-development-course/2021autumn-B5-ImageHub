package com.hyosakura.imagehub.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
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
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.pathString


@SuppressLint("Range")
class DeviceImageViewModel(private val repository: DataRepository) : ViewModel() {
    lateinit var dirList: LiveData<List<DeviceDirEntity>>

    companion object {
        private val map = mutableMapOf<Int, DeviceImageEntity>()
        val imageList: LiveData<Collection<DeviceImageEntity>>
            get() {
                return flow {
                    emit(map.values)
                }.asLiveData()
            }
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

    fun getDeviceImage() {
        val root = "/storage/emulated/0/"
        val path = arrayOf("$root/Download", "$root/Pictures")
        fun match(file: File): Boolean {
            return (
                    file.extension == "jpg" ||
                            file.extension == "png" ||
                            file.extension == "gif" ||
                            file.extension == "jpeg" ||
                            file.extension == "bmp"
                    )
        }
        var id = 1
        path.forEach {
            Files.walkFileTree(Paths.get(it), object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
                    if (match(file.toFile())) {
                        val size = file.toFile().length()
                        val bitmap = ImageUtil.decodeFile(
                            file.pathString,
                            if (size > 1024 * 500) 10 else 1
                        )
                        val image = object : DeviceImageEntity(
                            imageId = id++,
                            name = file.name,
                            url = file.pathString,
                            ext = file.extension,
                            width = bitmap.width,
                            height = bitmap.height,
                            size = file.toFile().length().toDouble(),
                            bitmap = bitmap
                        ) {}
                        map[image.imageId!!] = image
                    }
                    return super.visitFile(file, attrs)
                }
            })
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