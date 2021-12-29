package com.hyosakura.imagehub.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ImageManageViewModel(private val repository: DataRepository) : ViewModel() {
    companion object {
        val idToBitMap = mutableMapOf<Int, Bitmap>()
    }

    val allImages: Flow<List<ImageEntity>> = repository.allImages.map { list ->
        list.map {
            it.thumbnail = ImageUtil.getThumbnail(it.url!!)
            it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
            it
        }
    }

    val recentShareImages: Flow<List<ImageEntity>> = repository.recentShareImages.map { list ->
        list.map {
            it.thumbnail = ImageUtil.getThumbnail(it.url!!)
            it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
            it
        }
    }

    var image by mutableStateOf<Flow<ImageEntity>>(emptyFlow())
    fun visitImage(imageId: Int) {
        viewModelScope.launch {
            getTagList(imageId)
        }
        viewModelScope.launch {
            image = repository.getImageById(imageId).map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                if (idToBitMap.containsKey(imageId)) {
                    val bitmap = idToBitMap[imageId]
                    if (bitmap!!.height * bitmap.height < it.bitmap!!.height * it.bitmap!!.width) {
                        idToBitMap[imageId] = it.bitmap!!
                    }
                }
                idToBitMap.putIfAbsent(imageId, it.bitmap!!)
                it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                it
            }
        }
    }

    var tagList by mutableStateOf<Flow<List<TagEntity>>>(emptyFlow())
    private fun getTagList(imageId: Int) {
        repository.imageWithTag(imageId).map { list ->
            list.tags
        }.also {
            tagList = it
        }
    }

    fun updateImage(entity: ImageEntity) {
        viewModelScope.launch {
            repository.updateImage(entity)
        }
    }

    fun addTagToImage(image: ImageEntity, tag: TagEntity) {
        viewModelScope.launch {
            val relation = ImageTagCrossRef(image.imageId!!, tag.tagId!!)
            repository.updateTag(tag.also { it.modifyTime = System.currentTimeMillis() })
            repository.insertTagToImage(relation)
        }
    }

    fun addImageToFolder(images: List<ImageEntity>, folder: FolderEntity) {
        viewModelScope.launch {
            images.forEach {
                it.folderId = folder.folderId!!
            }
            repository.updateImage(*images.toTypedArray())
        }
    }

    fun removeTag(image: ImageEntity, tag: TagEntity) {
        viewModelScope.launch {
            val relation = ImageTagCrossRef(image.imageId!!, tag.tagId!!)
            repository.removeTagFromImage(relation)
        }
    }

    var imagesInTag by mutableStateOf<Flow<List<ImageEntity>>>(emptyFlow())
    fun getImagesInTag(tagId: Int) {
        viewModelScope.launch {
            imagesInTag = repository.imageInTag(tagId).map { ref ->
                ref.images.map {
                    it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                    it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                    it
                }
            }
        }
    }

    var imagesInDir by mutableStateOf<Flow<List<ImageEntity>>>(emptyFlow())
    fun getImagesInDir(dirId: Int) {
        viewModelScope.launch {
            imagesInDir = repository.folderWithImages(dirId).map { ref ->
                ref.images.map {
                    it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                    it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                    it
                }
            }
        }
    }

    var searchResult by mutableStateOf<Flow<List<ImageEntity>>>(emptyFlow())
    fun searchImage(condition: String) {
        viewModelScope.launch {
            searchResult = repository.searchImage(condition).map { list ->
                list.filter {
                  it.deleted == 0
                }.map {
                    it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                    it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                    it
                }
            }
        }
    }
}

class ImageManageViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageManageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageManageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}