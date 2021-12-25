package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ImageManageViewModel(private val repository: DataRepository) : ViewModel() {
    var imageList: LiveData<List<ImageEntity>> = allImages()

    lateinit var image: LiveData<ImageEntity>

    lateinit var tagList: LiveData<List<TagEntity>>

    fun allImages(): LiveData<List<ImageEntity>> {
        return repository.allImages.map { list ->
            list.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                it
            }
        }.asLiveData()
    }

    fun visitImage(imageId: Int): LiveData<ImageEntity> {
        return repository.getImageById(imageId).map {
            it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
            getTagList(it.imageId!!)
            it
        }.asLiveData().also {
            image = it
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

    fun removeTag(image: ImageEntity, tag: TagEntity) {
        viewModelScope.launch {
            val relation = ImageTagCrossRef(image.imageId!!, tag.tagId!!)
            repository.removeTagFromImage(relation)
        }
    }

    fun imagesInTag(tagId: Int): LiveData<List<ImageEntity>> {
        return repository.tagWithImages(tagId).map { ref ->
            ref.images.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                it
            }
        }.asLiveData().also {
            imageList = it
        }
    }

    fun imagesInDir(dirId: Int): LiveData<List<ImageEntity>> {
        return repository.dirWithImages(dirId).map { ref ->
            ref.images.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                it
            }
        }.asLiveData().also {
            imageList = it
        }
    }

    fun searchImage(condition: String): LiveData<List<ImageEntity>> {
        return repository.searchImage(condition).map { list ->
            list.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                it
            }
        }.asLiveData().also {
            imageList = it
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