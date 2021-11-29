package com.hyosakura.imagehub.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.repository.DataRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


class ImageListViewModel(private val repository: DataRepository) : ViewModel() {
    private val options = BitmapFactory.Options()

    /**
     * 指定日期内分享的所有图片集合
     */
    fun shareImageListBetweenDate(
        from: LocalDateTime,
        to: LocalDateTime
    ): LiveData<List<ImageEntity>> {
        return repository.shareImageListBetweenDate(from.toLong(), to.toLong()).asLiveData()
    }

    /**
     * 根据url返回图片的bitmap表示
     */
    fun getImageByUrl(url: String, size: Int): LiveData<Bitmap> {
        options.inSampleSize = size
        val bitmap = BitmapFactory.decodeFile(url, options)
        return flow<Bitmap> {
            emit(bitmap)
        }.asLiveData()
    }

    /**
     * 搜索结果的图片的添加日期
     */
    fun addDateOfSearchImage(key: String): LiveData<List<LocalDateTime>> {
        return repository.addDateOfSearchImage(key).map {
            it.map { entity ->
                entity.addTime!!.toDate()
            }
        }.asLiveData()
    }

    /**
     * 指定日期的搜索结果的图片集合
     */
    fun searchImageBetweenDate(
        from: LocalDateTime,
        to: LocalDateTime,
        key: String
    ): LiveData<List<ImageEntity>> {
        return repository.searchImageBetweenDate(key, from.toLong(), to.toLong()).asLiveData()
    }

    /**
     * 最近分享的图片
     */
    fun recentShareImage(): LiveData<List<ImageEntity>> {
        return repository.recentShareImage().asLiveData()
    }

    /**
     * 所有图片的添加日期
     */
    fun addDateOfAllImage(): LiveData<List<LocalDateTime>> {
        return repository.addDateOfAllImage().map {
            it.map { entity ->
                entity.addTime!!.toDate()
            }
        }.asLiveData()
    }

    /**
     * 指定日期范围内添加的所有图片集合
     */
    fun addImageListBetweenDate(
        from: LocalDateTime,
        to: LocalDateTime
    ): LiveData<List<ImageEntity>> {
        return repository.addImageListBetweenDate(from.toLong(), to.toLong()).asLiveData()
    }

    /**
     * 给图像添加标签
     */
    fun addTagToImage(imageIds: List<Int>, tagId: Int) {
        viewModelScope.launch {
            val relations = imageIds.map {
                ImageTagCrossRef(it, tagId)
            }
            repository.addTagToImage(*relations.toTypedArray())
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

    private fun Long.toDate(): LocalDateTime {
        val instant = Instant.ofEpochMilli(this)
        val zone = ZoneId.systemDefault()
        return LocalDateTime.ofInstant(instant, zone)
    }

    private fun LocalDateTime.toLong(): Long {
        val zone = ZoneId.systemDefault()
        val instant: Instant = this.atZone(zone).toInstant()
        return instant.toEpochMilli()
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