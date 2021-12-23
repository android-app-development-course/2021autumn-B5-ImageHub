package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import kotlinx.coroutines.launch

class RecycleBinViewModel(private val repository: DataRepository) : ViewModel() {
    val allDeletedImages = repository.allDeletedImages.asLiveData()

    fun deleteAllImages() {
        viewModelScope.launch {
            val images = repository.getAllDeletedImagesWithoutFlow()
            deleteImages(*images.toTypedArray())
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