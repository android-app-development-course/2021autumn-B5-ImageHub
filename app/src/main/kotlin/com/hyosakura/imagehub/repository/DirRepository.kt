package com.hyosakura.imagehub.repository

import com.hyosakura.imagehub.dao.DirDao
import com.hyosakura.imagehub.entity.DirEntity
import kotlinx.coroutines.flow.Flow

class DirRepository(private val dao : DirDao) {
    val allDirs: Flow<List<DirEntity>> = dao.getAllDirs()
}