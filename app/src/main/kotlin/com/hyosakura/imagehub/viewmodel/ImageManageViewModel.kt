package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ImageManageViewModel(private val repository: DataRepository) : ViewModel() {
    init {
        allImages()
        searchImage("")
    }

    lateinit var imageList: LiveData<List<ImageEntity>>

    lateinit var searchResult: LiveData<List<ImageEntity>>

    lateinit var image: LiveData<ImageEntity>

    lateinit var tagList: LiveData<List<TagEntity>>

    private fun allImages() {
        viewModelScope.launch {
            imageList = repository.allImages.map { list ->
                list.map {
                    it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                    it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                    it
                }
            }.asLiveData()
        }
    }

    fun visitImage(imageId: Int) {
        viewModelScope.launch {
            getTagList(imageId)
        }
        viewModelScope.launch {
            image = repository.getImageById(imageId).map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                it
            }.asLiveData()
        }
    }

    private fun getTagList(imageId: Int) {
        repository.imageWithTag(imageId).map { list ->
            list.tags
        }.asLiveData().also {
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

    fun imagesInTag(tagId: Int) {
        viewModelScope.launch {
            imageList = repository.tagWithImages(tagId).map { ref ->
                ref.images.map {
                    it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                    it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                    it
                }
            }.asLiveData()
        }
    }

    fun imagesInDir(dirId: Int) {
        viewModelScope.launch {
            imageList = repository.dirWithImages(dirId).map { ref ->
                ref.images.map {
                    it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                    it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                    it
                }
            }.asLiveData()
        }
    }

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
            }.asLiveData()
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