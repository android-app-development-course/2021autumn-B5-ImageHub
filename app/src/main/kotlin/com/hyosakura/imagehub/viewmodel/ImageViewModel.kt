package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.ImageRepository

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {
    val allDirs: LiveData<List<ImageEntity>> = repository.allImages.asLiveData()
}

class ImageViewModelFactory(private val repository: ImageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}