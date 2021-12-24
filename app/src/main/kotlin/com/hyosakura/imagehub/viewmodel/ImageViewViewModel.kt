package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ImageViewViewModel(private val repository: DataRepository) : ViewModel() {
    lateinit var image: LiveData<ImageEntity>

    fun getImageById(id: Int): LiveData<ImageEntity> {
        return repository.getImageById(id).map {
            it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
            it
        }.asLiveData().also {
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
        if (modelClass.isAssignableFrom(ImageViewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageViewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}