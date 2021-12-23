package com.hyosakura.imagehub.repository

import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.util.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DataRepository(private val database: AppDatabase) {
    fun getImageById(id: Int) = database.imageDao().getImageById(id)

    fun getDirById(dirId: Int) = database.dirDao().getDirById(dirId)

    suspend fun insertImage(vararg entity: ImageEntity) = database.imageDao().updateImages(*entity)

    suspend fun updateImage(vararg entity: ImageEntity) = database.imageDao().updateImages(*entity)

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

    suspend fun moveImageToRecycle(vararg images: ImageEntity) =
        database.imageDao().updateImages(*images)

    fun recentTagWithImages(limit: Int) = database.tagDao().recentTagWithImages(limit)

    fun recentDirWithImages(limit: Int) = database.dirDao().recentDirWithImages(limit)

    val allTags = database.tagDao().getAllTags(20)

    fun tagWithImages(tagId: Int) = database.tagDao().getTagWithImagesById(tagId)

    suspend fun updateTag(vararg tags: TagEntity) = database.tagDao().updateTags(*tags)

    suspend fun insertTag(vararg tags: TagEntity) = database.tagDao().insertTags(*tags)

    suspend fun deleteTag(vararg tags: TagEntity) = database.tagDao().deleteTags(*tags)

    suspend fun updateDir(vararg dirs: DirEntity) = database.dirDao().updateDirs(*dirs)

    suspend fun insertDir(vararg dirs: DirEntity) = database.dirDao().insertDirs(*dirs)

    val allImages = database.imageDao().getAllImages(20)

    val allDeletedImages = database.imageDao().getAllDeletedImages()

    fun getAllDeletedImagesWithoutFlow() = database.imageDao().getAllDeletedImagesWithoutFlow()

    fun dirWithImages(dirId: Int) = database.dirDao().getDirWithImagesById(dirId)

    fun childDir(dirId: Int) = database.dirDao().getChildDirs(dirId)

    fun imageNumInTag(tagId: Int) = database.tagDao().getTagWithImagesById(tagId)

    suspend fun removeImagesInRecycleBin(vararg images: ImageEntity) =
        database.imageDao().removeDeletedImages(*images)
}