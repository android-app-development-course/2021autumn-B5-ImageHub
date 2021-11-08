package com.hyosakura.imagehub.repository

import com.hyosakura.imagehub.dao.TagDao
import com.hyosakura.imagehub.entity.TagEntity
import kotlinx.coroutines.flow.Flow

class TagRepository(private val dao : TagDao) {
    val allDirs: Flow<List<TagEntity>> = dao.getAllTags()
}