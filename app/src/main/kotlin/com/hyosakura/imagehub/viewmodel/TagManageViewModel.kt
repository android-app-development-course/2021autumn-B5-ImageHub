package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.repository.DataRepository
import kotlinx.coroutines.launch

class TagManageViewModel(private val repository: DataRepository) : ViewModel() {
    val allTags = repository.allTags.asLiveData()

    fun updateTag(entity: TagEntity) {
        viewModelScope.launch {
            repository.updateTag(entity)
        }
    }

    fun insertTag(tagName: String) {
        viewModelScope.launch {
            val entity = TagEntity(tagId = null, name = tagName, addTime = System.currentTimeMillis())
            repository.insertTag(entity)
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