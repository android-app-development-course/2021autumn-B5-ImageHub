package com.hyosakura.imagehub.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FolderManageViewModel(private val repository: DataRepository) : ViewModel() {
    var currentFolder by mutableStateOf<Flow<FolderEntity>>(emptyFlow())
    var currentChildFolder by mutableStateOf<Flow<List<FolderEntity>>>(emptyFlow())
    var imagesInCurrentFolder by mutableStateOf<Flow<List<ImageEntity>>>(emptyFlow())
    var folderById by mutableStateOf<Flow<FolderEntity>>(emptyFlow())

    fun visitFolder(FolderId: Int) {
        viewModelScope.launch {
            visitChildFolder(FolderId)
        }
        viewModelScope.launch {
            visitImages(FolderId)
        }
        viewModelScope.launch {
            repository.getFolderById(FolderId).also {
                currentFolder = it
            }
        }
    }

    private fun visitChildFolder(FolderId: Int) {
        currentChildFolder = repository.childFolder(FolderId).map { outerList ->
            outerList.childDirs.onEach {
                it.latestPicture = repository.folderWithImages(it.folderId!!)
                    .firstOrNull()?.images?.firstOrNull()?.url?.let { s ->
                        ImageUtil.getThumbnail(s)
                    }
            }
        }
    }

    private fun visitImages(folderId: Int) {
        imagesInCurrentFolder = repository.folderWithImages(folderId).map { relation ->
            relation.images.filter {
                it.deleted == 0
            }.map {
                it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                it
            }
        }
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

     fun getFolderById(id: Int) {
         folderById = repository.getFolderById(id)
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