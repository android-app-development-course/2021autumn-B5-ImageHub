package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DirManageViewModel(private val repository: DataRepository) : ViewModel() {
    lateinit var currentDir: LiveData<DirEntity>
    lateinit var currentChildDir: LiveData<List<DirEntity>>
    lateinit var imagesInCurrentDir: LiveData<List<ImageEntity>>


    fun visitDir(dirId: Int): LiveData<DirEntity> {
        imagesInCurrentDir = repository.dirWithImages(dirId).map { relation ->
            relation.images.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 100)
                it
            }
        }.asLiveData()
        currentChildDir = repository.childDir(dirId).map { list ->
            list.map {
                it.dir
            }
        }.asLiveData()
        return repository.getDirById(dirId).asLiveData().also {
            currentDir = it
        }
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
            val time = System.currentTimeMillis()
            sourceDir.modifyTime = time
            targetDir.modifyTime = time
            repository.updateDir(sourceDir, targetDir)
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