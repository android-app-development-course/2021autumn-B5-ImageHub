package com.hyosakura.imagehub.repository

import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.util.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DataRepository(private val database: AppDatabase) {
    fun getImageById(id: Int): ImageEntity {
        return database.imageDao().getImageById(id)
    }

    fun searchImage(condition: String): Flow<List<ImageEntity>> {
        val flow1 = database.imageDao().searchImage(condition)
        val flow2 = database.tagDao().searchImage(condition)
        val flow3 = database.dirDao().searchImage(condition)
        return flow1.combine(flow2) { a, b ->
            a.apply {
                addAll(
                    b.flatMap {
                        it.images
                    }
                )
            }
        }.combine(flow3) { a, b ->
            a.apply {
                addAll(
                    b.flatMap {
                        it.images
                    }
                )
            }
        }
    }

    fun moveImageToRecycle(vararg images: ImageEntity) = database.imageDao().updateImages(*images)

    fun recentTagWithImages(limit: Int) = database.tagDao().recentTagWithImages(limit)

    fun recentDirWithImages(limit: Int) = database.dirDao().recentDirWithImages(limit)

    fun getAllTags() = database.tagDao().getAllTags()

    fun imageNumInTag(tagId: Int) = database.tagDao().getTagWithImagesById(tagId)
}