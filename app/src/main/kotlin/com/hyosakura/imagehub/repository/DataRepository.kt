package com.hyosakura.imagehub.repository

import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.util.AppDatabase

class DataRepository(private val database: AppDatabase) {
    fun shareImageListBetweenDate(from: Long, to: Long) =
        database.imageDao().shareImageListBetweenDate(from, to)

    fun addDateOfSearchImage(key: String) = database.imageDao().addDateOfSearchImage(key)

    fun searchImageBetweenDate(key: String, from: Long, to: Long) =
        database.imageDao().searchImageBetweenDate(key, from, to)

    fun recentShareImage() = database.imageDao().recentShareImage()

    fun addDateOfAllImage() = database.imageDao().addDateOfAddImage()

    fun addImageListBetweenDate(from: Long, to: Long) =
        database.imageDao().addImageListBetweenDate(from, to)

    fun addTagToImage(vararg relation : ImageTagCrossRef) = database.imageDao().insertTags(*relation)

    fun moveImageToRecycle(vararg images: ImageEntity) = database.imageDao().updateImages(*images)
}