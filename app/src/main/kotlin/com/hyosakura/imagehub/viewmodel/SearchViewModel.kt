package com.hyosakura.imagehub.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil

class SearchViewModel(private val repository: DataRepository) : ViewModel() {
    /**
     * 最近使用标签的列表，包含该标签的第一张图片
     */
    fun recentTagWithImages(limit: Int = 5, size: Int): List<Pair<String, Bitmap>> {
        return repository.recentTagWithImages(limit).map {
            it.tag.name!! to ImageUtil.decodeFile(it.images.first().url!!, size)
        }
    }

    /**
     * 最近使用的文件夹，包含该文件夹的第一张图片
     */
    fun recentDirWithImages(limit: Int = 5, size: Int): List<Pair<String, Bitmap>> {
        return repository.recentDirWithImages(limit).map {
            it.dir.name!! to ImageUtil.decodeFile(it.images.first().url!!, size)
        }
    }

    fun fakeTags(ids: List<Int>): List<TagEntity> {
        return ids.map {
            TagEntity(tagId = it, name = it.toString())
        }
    }

    fun fakeDirs(ids: List<Int>): List<DirEntity> {
        return ids.map {
            DirEntity(dirId = it, parentId = -1, name = it.toString())
        }
    }
}

class SearchViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}