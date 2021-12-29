package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecycleBinViewModel(private val repository: DataRepository) : ViewModel() {
    val allDeletedImages: Flow<List<ImageEntity>> = repository.allDeletedImages.map { list ->
        list.map {
            it.thumbnail = ImageUtil.getThumbnail(it.url!!)
            it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
            it
        }
    }

    fun deleteAllImages() {
        viewModelScope.launch {
            allDeletedImages.collect {
                deleteImages(*it.toTypedArray())
            }
        }
    }

    fun deleteImages(vararg images: ImageEntity) {
        viewModelScope.launch {
            repository.removeImagesInRecycleBin(*images)
        }
    }
}

class RecycleBinViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecycleBinViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecycleBinViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}