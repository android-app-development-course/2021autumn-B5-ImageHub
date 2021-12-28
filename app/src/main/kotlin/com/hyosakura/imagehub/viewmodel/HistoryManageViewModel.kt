package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hyosakura.imagehub.entity.HistoryEntity
import com.hyosakura.imagehub.repository.DataRepository
import kotlinx.coroutines.launch

class HistoryManageViewModel(private val repository: DataRepository) : ViewModel() {
    val searchHistories = repository.searchHistories

    fun addHistory(history: HistoryEntity) {
        viewModelScope.launch {
            repository.insertHistories(history)
        }
    }

    fun deleteHistory(history: HistoryEntity) {
        viewModelScope.launch {
            repository.deleteHistories(history)
        }
    }
}

class HistoryManageViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryManageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryManageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}