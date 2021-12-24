package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DirManageViewModel(private val repository: DataRepository) : ViewModel() {
    var currentDir: LiveData<DirEntity> = visitDir(-1)
    lateinit var currentChildDir: LiveData<List<DirEntity>>
    lateinit var imagesInCurrentDir: LiveData<List<ImageEntity>>

    fun visitDir(dirId: Int): LiveData<DirEntity> {
        visitChildDir(dirId)
        visitImages(dirId)
        return repository.getDirById(dirId).asLiveData().also {
            currentDir = it
        }
    }

    private fun visitChildDir(dirId : Int) {
        currentChildDir = repository.childDir(dirId).map { outerList ->
            outerList.childDirs.also { innerList ->
                innerList.forEach {
                    it.latestPicture = repository.dirWithImages(it.dirId!!)
                        .firstOrNull()?.images?.firstOrNull()?.url?.let { s ->
                            ImageUtil.decodeFile(s, 100)
                        }
                }
            }
        }.asLiveData()
    }

    private fun visitImages(dirId: Int) {
        imagesInCurrentDir = repository.dirWithImages(dirId).map { relation ->
            relation.images.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                it
            }
        }.asLiveData()
    }

    fun newDir(name: String) {
        viewModelScope.launch {
            val entity = DirEntity(
                dirId = null,
                parentId = -1,
                name = name,
                number = 0,
                modifyTime = System.currentTimeMillis()
            )
            repository.insertDir(entity)
        }
    }

    fun moveDir(sourceDir: DirEntity, targetDir: DirEntity) {
        viewModelScope.launch {
            sourceDir.parentId = targetDir.dirId!!
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