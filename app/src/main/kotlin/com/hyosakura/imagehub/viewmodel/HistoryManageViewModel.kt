package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hyosakura.imagehub.entity.HistoryEntity
import com.hyosakura.imagehub.repository.DataRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HistoryManageViewModel(private val repository: DataRepository) : ViewModel() {
    companion object {
        val searchListCache = mutableListOf<String>()
    }

    val searchHistories = repository.searchHistories.map { list ->
        list.map {
            val keyword = it.keyword!!
            if (searchListCache.contains(keyword)) {
                searchListCache.add(keyword)
            }
            it
        }
        list
    }

    fun addHistory(history: String) {
        viewModelScope.launch {
            repository.insertHistories(
                HistoryEntity(
                    keyword = history,
                    addTime = System.currentTimeMillis()
                )
            )
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