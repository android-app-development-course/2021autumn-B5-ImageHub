package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FolderManageViewModel(private val repository: DataRepository) : ViewModel() {
    var currentFolder: LiveData<FolderEntity> = visitFolder(-1)
    lateinit var currentChildFolder: LiveData<List<FolderEntity>>
    lateinit var imagesInCurrentFolder: LiveData<List<ImageEntity>>

    fun visitFolder(dirId: Int): LiveData<FolderEntity> {
        visitChildFolder(dirId)
        visitImages(dirId)
        return repository.getDirById(dirId).asLiveData().also {
            currentFolder = it
        }
    }

    private fun visitChildFolder(dirId : Int) {
        currentChildFolder = repository.childDir(dirId).map { outerList ->
            outerList.childDirs.onEach {
                it.latestPicture = repository.dirWithImages(it.folderId!!)
                    .firstOrNull()?.images?.firstOrNull()?.url?.let { s ->
                        ImageUtil.getThumbnail(s)
                    }
            }
        }.asLiveData()
    }

    private fun visitImages(folderId: Int) {
        imagesInCurrentFolder = repository.dirWithImages(folderId).map { relation ->
            relation.images.filter {
                it.deleted == 0
            }.map {
                it.bitmap = ImageUtil.getThumbnail(it.url!!)
                it
            }
        }.asLiveData()
    }

    fun newFolder(name: String) {
        viewModelScope.launch {
            val entity = FolderEntity(
                folderId = null,
                parentId = -1,
                name = name,
                number = 0,
                modifyTime = System.currentTimeMillis()
            )
            repository.insertDir(entity)
        }
    }

    fun moveFolder(sourceFolder: FolderEntity, targetFolder: FolderEntity) {
        viewModelScope.launch {
            sourceFolder.parentId = targetFolder.folderId!!
            val time = System.currentTimeMillis()
            sourceFolder.modifyTime = time
            targetFolder.modifyTime = time
            repository.updateDir(sourceFolder, targetFolder)
        }
    }
}

class FolderManageViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FolderManageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FolderManageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}