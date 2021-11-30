package com.hyosakura.imagehub.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.toDate
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ImageListViewModel(private val repository: DataRepository) : ViewModel() {
    /**
     * 根据id返回图片的bitmap表示
     */
    fun getImageByUrl(imageId: Int, size: Int): Bitmap {
        return ImageUtil.decodeFile(repository.getImageById(imageId).url!!, size)
    }

    /**
     * 特定条件的图片
     */
    suspend fun getImagesByCondition(
        condition: String?
    ): Map<LocalDate, List<ImageEntity>> = withContext(viewModelScope.coroutineContext) {
        val map = mutableMapOf<LocalDate, MutableList<ImageEntity>>()
        map.apply {
            if (condition != null) {
                repository.searchImage(condition).collect { outer ->
                    outer.forEach { inner ->
                        val date = inner.addTime!!.toDate().toLocalDate()
                        computeIfAbsent(date) {
                            mutableListOf()
                        }.add(inner)
                    }
                }
            } else {
                repository.getAllImages().collect { outer ->
                    outer.forEach { inner ->
                        val date = inner.addTime!!.toDate().toLocalDate()
                        computeIfAbsent(date) {
                            mutableListOf()
                        }.add(inner)
                    }
                }
            }
        }

    }

    /**
     * 图片移入回收站
     */
    fun moveImageToRecycle(imageIds: List<Int>) {
        viewModelScope.launch {
            val updateEntity = imageIds.map {
                ImageEntity(imageId = it, deleted = 1)
            }
            repository.moveImageToRecycle(*updateEntity.toTypedArray())
        }
    }

    /**
     * 返回供测试用的纯色图片
     */
    fun fakeImages(width: Int, height: Int, ids: List<Int>): Map<LocalDate, List<Bitmap>> {
        return mapOf(
            LocalDate.now() to ids.map {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bitmap.eraseColor(it)
                bitmap
            }
        )
    }
}

class ImageListViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}