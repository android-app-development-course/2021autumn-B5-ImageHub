package com.hyosakura.imagehub.repository

import com.hyosakura.imagehub.dao.ImageDao
import com.hyosakura.imagehub.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

class ImageRepository(private val dao : ImageDao) {
    val allImages: Flow<List<ImageEntity>> = dao.getAllImages()
}