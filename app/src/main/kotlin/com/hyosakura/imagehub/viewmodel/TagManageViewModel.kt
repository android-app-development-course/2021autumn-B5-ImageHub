package com.hyosakura.imagehub.viewmodel

import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TagManageViewModel(private val repository: DataRepository) : ViewModel() {
    val allTags = repository.allTags.asLiveData()
    val starTags = repository.starTag.asLiveData()
    lateinit var candidateTagWithName: LiveData<List<TagEntity>>

    fun updateTag(entity: TagEntity) {
        viewModelScope.launch {
            repository.updateTag(entity)
        }
    }

    fun getImageInTag(tagId: Int): LiveData<List<ImageEntity>> {
        return repository.imageInTag(tagId).map { relation ->
            relation.images.filter {
                it.deleted == 0
            }.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 1)
                it
            }
        }.asLiveData()
    }

    fun getRecentTag(num: Int): LiveData<List<TagEntity>> {
        return repository.recentTag(num).asLiveData()
    }

    fun insertTag(tag: TagEntity) {
        viewModelScope.launch {
            tag.addTime = System.currentTimeMillis()
            repository.insertTag(tag)
        }
    }

    suspend fun insertTagAndGetId(tag: TagEntity): List<Long> {
        return withContext(viewModelScope.coroutineContext) {
            tag.addTime = System.currentTimeMillis()
            repository.insertTag(tag)
        }
    }

    fun getTagByName(name: String, fuzz: Boolean = true): LiveData<List<TagEntity>> {
        return repository.getTagByName(if (fuzz) "%$name%" else name).asLiveData().also {
            candidateTagWithName = it
        }
    }

    suspend fun getTagByNameWithOutFlow(name: String, fuzz: Boolean = true): List<TagEntity> =
        withContext(viewModelScope.coroutineContext) {
            repository.getTagByNameWithOutFlow(if (fuzz) "%$name%" else name)
        }

    fun deleteTag(entity: TagEntity) {
        viewModelScope.launch {
            repository.deleteTag(entity)
        }
    }
}

class TagManageViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagManageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TagManageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}