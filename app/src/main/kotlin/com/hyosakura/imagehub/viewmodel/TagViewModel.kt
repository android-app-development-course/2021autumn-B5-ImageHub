package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.repository.TagRepository

class TagViewModel(private val repository: TagRepository) : ViewModel() {
    val allDirs: LiveData<List<TagEntity>> = repository.allDirs.asLiveData()
}

class TagViewModelFactory(private val repository: TagRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}