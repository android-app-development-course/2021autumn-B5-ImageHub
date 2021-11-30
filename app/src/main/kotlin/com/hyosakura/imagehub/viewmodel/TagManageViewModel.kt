package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.hyosakura.imagehub.repository.DataRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TagManageViewModel(private val repository: DataRepository) : ViewModel() {
    val allTags = repository.getAllTags().asLiveData()

    fun imageNumInTag(tagId: Int): Int {
        return runBlocking {
            repository.imageNumInTag(tagId).first().images.size
        }
    }
}

class TagManageViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagManageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagManageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}