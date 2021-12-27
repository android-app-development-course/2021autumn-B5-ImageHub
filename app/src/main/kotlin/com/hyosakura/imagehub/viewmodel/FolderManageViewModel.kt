package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FolderManageViewModel(private val repository: DataRepository) : ViewModel() {
    var currentFolder: LiveData<FolderEntity> = visitFolder(-1)
    lateinit var currentChildFolder: LiveData<List<FolderEntity>>
    lateinit var imagesInCurrentFolder: LiveData<List<ImageEntity>>
    var folderById: LiveData<FolderEntity> = getFolderById(-1)

    fun visitFolder(dirId: Int): LiveData<FolderEntity> {
        viewModelScope.launch {
            visitChildFolder(dirId)
        }
        viewModelScope.launch {
            visitImages(dirId)
        }
        return repository.getDirById(dirId).asLiveData().also {
            currentFolder = it
        }
    }

    private fun visitChildFolder(dirId: Int) {
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
                it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
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

    suspend fun newFolderAndGetId(name: String): Int = withContext(viewModelScope.coroutineContext) {
        val entity = FolderEntity(
            folderId = null,
            parentId = -1,
            name = name,
            number = 0,
            modifyTime = System.currentTimeMillis()
        )
        repository.insertDir(entity).first().toInt()
    }

     fun getFolderById(id: Int): LiveData<FolderEntity> {
        return repository.getDirById(id).asLiveData().also {
            folderById = it
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