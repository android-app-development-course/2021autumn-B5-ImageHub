package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.repository.DirRepository

class DirViewModel(private val repository: DirRepository) : ViewModel() {
    val allDirs: LiveData<List<DirEntity>> = repository.allDirs.asLiveData()
}

class DirViewModelFactory(private val repository: DirRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DirViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DirViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}