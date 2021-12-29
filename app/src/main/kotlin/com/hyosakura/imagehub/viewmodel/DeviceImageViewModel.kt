package com.hyosakura.imagehub.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.*
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
    companion object {
        private val map = mutableMapOf<Int, DeviceImageEntity>()
        val imageList: LiveData<Collection<DeviceImageEntity>>
            get() {
                return flow {
                    emit(map.values)
                }.asLiveData()
            }
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
                            file.extension == "bmp" ||
                            file.extension == "webp"
                    )
        }
        var id = 1
        path.forEach {
            Files.walkFileTree(Paths.get(it), object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
                    if (match(file.toFile())) {
                        val size = file.toFile().length()
                        val thumbnail = ImageUtil.getThumbnail(file.pathString)
                        val image = object : DeviceImageEntity(
                            imageId = id++,
                            name = file.name,
                            url = file.pathString,
                            ext = file.extension,
                            width = thumbnail.width,
                            height = thumbnail.height,
                            size = size.toDouble(),
                            bitmap = thumbnail
                        ) {}
                        map[image.imageId!!] = image
                    }
                    return super.visitFile(file, attrs)
                }
            })
        }
    }

    suspend fun importImage(context: Context, image: DeviceImageEntity): Int =
        withContext(viewModelScope.coroutineContext) {
            val path = ImageUtil.saveImageToLocalStorge(context, ImageUtil.decodeFile(image.url!!, 1), image.name!!)
            println(path)
            val entity = ImageEntity(
                imageId = null,
                name = image.name,
                url = path,
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