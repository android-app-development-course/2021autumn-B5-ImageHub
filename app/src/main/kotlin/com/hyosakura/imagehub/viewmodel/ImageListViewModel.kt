package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.map

class ImageListViewModel(private val repository: DataRepository) : ViewModel() {
    lateinit var imageList: LiveData<List<ImageEntity>>

    fun allImages(): LiveData<List<ImageEntity>> {
        return repository.allImages.map { list ->
            list.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 100)
                it
            }
        }.asLiveData().also {
            imageList = it
        }
    }

    fun imagesWithTag(tagId: Int): LiveData<List<ImageEntity>>  {
        return repository.tagWithImages(tagId).map { ref ->
            ref.images.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 100)
                it
            }
        }.asLiveData().also {
            imageList = it
        }
    }

    fun imagesInDir(dirId: Int): LiveData<List<ImageEntity>> {
        return repository.dirWithImages(dirId).map { ref ->
            ref.images.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 100)
                it
            }
        }.asLiveData().also {
            imageList = it
        }
    }

    fun searchImage(condition: String): LiveData<List<ImageEntity>>  {
        return repository.searchImage(condition).map { list ->
            list.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 100)
                it
            }
        }.asLiveData().also {
            imageList = it
        }
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