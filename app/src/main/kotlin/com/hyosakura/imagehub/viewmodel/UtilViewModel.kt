package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyosakura.imagehub.repository.DataRepository

class UtilViewModel(private val repository: DataRepository) : ViewModel() {

}

class UtilViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UtilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UtilViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}