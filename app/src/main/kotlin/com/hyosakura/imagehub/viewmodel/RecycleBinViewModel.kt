package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyosakura.imagehub.repository.DataRepository

class RecycleBinViewModel(private val repository: DataRepository) : ViewModel() {

}

class RecycleBinViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecycleBinViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecycleBinViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}