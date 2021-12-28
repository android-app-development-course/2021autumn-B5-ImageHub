package com.hyosakura.imagehub.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TagManageViewModel(private val repository: DataRepository) : ViewModel() {
    val allTags = repository.allTags.map { list ->
        list.map {
            it.latestPicture = repository.imageInTag(it.tagId!!).first().images.firstOrNull()?.let {
                ImageUtil.decodeFile(it.url!!, 1)
            }
            it
        }
    }
    val starTags: Flow<List<TagEntity>> = repository.starTag.map { list ->
        list.map {
            it.latestPicture = repository.imageInTag(it.tagId!!).first().images.firstOrNull()?.let {
                ImageUtil.decodeFile(it.url!!, 1)
            }
            it
        }
    }

    fun updateTag(entity: TagEntity) {
        viewModelScope.launch {
            repository.updateTag(entity)
        }
    }

    var tag by mutableStateOf<Flow<TagEntity>>(emptyFlow())
    fun visitTag(tagId: Int): Flow<TagEntity> {
        return repository.getTagById(tagId).also {
            tag = it
        }
    }

    fun getImageInTag(tagId: Int): LiveData<List<ImageEntity>> {
        return repository.imageInTag(tagId).map { relation ->
            relation.images.filter {
                it.deleted == 0
            }.map {
                it.bitmap = ImageUtil.decodeFile(it.url!!, 2)
                it.thumbnail = ImageUtil.getThumbnail(it.url!!)
                it
            }
        }.asLiveData()
    }

    val recentTags = repository.recentTag(20).map { list ->
        list.map { t ->
            t.latestPicture =
                repository.imageInTag(t.tagId!!).first().images.firstOrNull()?.let { i ->
                    ImageUtil.decodeFile(i.url!!, 1)
                }
            t
        }
    }

    fun insertTag(tag: TagEntity) {
        viewModelScope.launch {
            tag.addTime = System.currentTimeMillis()
            tag.modifyTime = System.currentTimeMillis()
            repository.insertTag(tag)
        }
    }

    suspend fun insertTagAndGetId(tag: TagEntity): List<Long> {
        return withContext(viewModelScope.coroutineContext) {
            tag.addTime = System.currentTimeMillis()
            repository.insertTag(tag)
        }
    }

    var candidateTagWithName by mutableStateOf<Flow<List<TagEntity>>>(emptyFlow())
    fun getTagByName(name: String, fuzz: Boolean = true): Flow<List<TagEntity>> {
        return repository.getTagByName(if (fuzz) "%$name%" else name).also {
            candidateTagWithName = it
        }
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