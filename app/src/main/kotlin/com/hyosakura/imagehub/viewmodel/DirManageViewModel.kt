package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.repository.DataRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DirManageViewModel(private val repository: DataRepository) : ViewModel() {
    fun currentDir(dirId: Int): LiveData<DirEntity> {
        return repository.getDirById(dirId).asLiveData()
    }

    fun currentChildDir(dirId: Int): LiveData<List<DirEntity>> {
        return repository.childDir(dirId).map { list ->
            list.map {
                it.dir
            }
        }.asLiveData()
    }

    fun newDir(name: String) {
        viewModelScope.launch {
            val entity = DirEntity(
                dirId = null,
                parentId = null,
                name = name,
                number = 0,
                modifyTime = System.currentTimeMillis()
            )
            repository.insertDir(entity)
        }
    }

    fun moveDir(sourceDir: DirEntity, targetDir: DirEntity) {
        viewModelScope.launch {
            sourceDir.parentId = targetDir.dirId
            repository.updateDir(sourceDir)
        }
    }
}

class DirManageViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DirManageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DirManageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}