package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import kotlinx.coroutines.launch

class ImageViewViewModel(private val repository: DataRepository) : ViewModel() {
    lateinit var image: LiveData<ImageEntity>

    fun getImageById(id: Int): LiveData<ImageEntity> {
        return repository.getImageById(id).asLiveData().also {
            image = it
        }
    }

    fun updateImage(entity: ImageEntity) {
        viewModelScope.launch {
            repository.updateImage(entity)
        }
    }
}

class ImageViewViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageViewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}