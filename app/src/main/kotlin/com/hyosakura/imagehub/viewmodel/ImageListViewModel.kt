package com.hyosakura.imagehub.viewmodel

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.toDate
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.random.Random

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
    ): Map<LocalDate, List<Int>> = withContext(viewModelScope.coroutineContext) {
        val map = mutableMapOf<LocalDate, MutableList<Int>>()
        map.apply {
            if (condition != null) {
                repository.searchImage(condition).collect { outer ->
                    outer.forEach { inner ->
                        val date = inner.addTime!!.toDate().toLocalDate()
                        computeIfAbsent(date) {
                            mutableListOf()
                        }.add(inner.imageId!!)
                    }
                }
            } else {
                repository.getAllImages().collect { outer ->
                    outer.forEach { inner ->
                        val date = inner.addTime!!.toDate().toLocalDate()
                        computeIfAbsent(date) {
                            mutableListOf()
                        }.add(inner.imageId!!)
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
     * 图片列表测试
     */
    fun fakeImageIdList(): Map<String, List<Int>> {
        val firstList = mutableListOf<Int>().also {
            for (i in 0..Random.nextInt(5, 9)) {
                it.add(114514)
            }
        }
        val secondList = mutableListOf<Int>().also {
            for (i in 0..Random.nextInt(5, 9)) {
                it.add(114514)
            }
        }
        return mapOf(
            "1" to firstList,
            "2" to secondList
        )
    }

    /**
     * 测试通过id拿到bitmap表示的图片
     */
    fun fakeGetBitMapById(id: Int): Bitmap {
        return BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_outline_image_24)
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