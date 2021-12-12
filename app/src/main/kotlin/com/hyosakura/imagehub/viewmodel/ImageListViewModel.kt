package com.hyosakura.imagehub.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.map

class ImageListViewModel(private val repository: DataRepository) : ViewModel() {
    val imageList = repository.allImages.map { list ->
        list.map {
            it to ImageUtil.decodeFile(it.url!!, 100)
        }
    }.asLiveData()

    fun imagesWithTag(tagId: Int): LiveData<List<Pair<ImageEntity, Bitmap>>> {
        return repository.tagWithImages(tagId).map { ref ->
            ref.images.map {
                it to ImageUtil.decodeFile(it.url!!, 100)
            }
        }.asLiveData()
    }

    fun imagesInDir(dirId: Int): LiveData<List<Pair<ImageEntity, Bitmap>>> {
        return repository.dirWithImages(dirId).map { ref ->
            ref.images.map {
                it to ImageUtil.decodeFile(it.url!!, 100)
            }
        }.asLiveData()
    }

    fun searchImage(condition: String): LiveData<List<Pair<ImageEntity, Bitmap>>> {
        return repository.searchImage(condition).map { list ->
            list.map {
                it to ImageUtil.decodeFile(it.url!!, 100)
            }
        }.asLiveData()
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