package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecycleBinViewModel(private val repository: DataRepository) : ViewModel() {
    val allDeletedImages: LiveData<List<ImageEntity>>
        get() {
            return repository.allDeletedImages.map { list ->
                list.map {
                    it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                    it
                }
            }.asLiveData()
        }

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