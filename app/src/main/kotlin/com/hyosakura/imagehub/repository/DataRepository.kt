package com.hyosakura.imagehub.repository

import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.HistoryEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.util.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DataRepository(private val database: AppDatabase) {
    fun getImageById(id: Int) = database.imageDao().getImageById(id)

    fun getFolderById(dirId: Int) = database.folderDao().getFolderById(dirId)

    suspend fun insertImage(vararg entity: ImageEntity) = database.imageDao().insertImages(*entity)

    suspend fun updateImage(vararg entity: ImageEntity) = database.imageDao().updateImages(*entity)

    fun searchImage(condition: String): Flow<List<ImageEntity>> {
        val flow1 = database.imageDao().searchImage("%$condition%")
        val flow2 = database.tagDao().searchImage("%$condition%")
        val flow3 = database.folderDao().searchImage("%$condition%")
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

    val starTag = database.tagDao().starTag()

    fun recentTag(limit: Int) = database.tagDao().recentTag(limit)

    val allTags = database.tagDao().getAllTags()

    fun getTagById(tagId: Int) = database.tagDao().getTagById(tagId)

    fun getTagByName(name: String) = database.tagDao().getTagByName(name)

    suspend fun insertTagToImage(vararg relation: ImageTagCrossRef) =
        database.imageDao().insertTags(*relation)

    suspend fun removeTagFromImage(vararg relation: ImageTagCrossRef) =
        database.imageDao().removeTags(*relation)

    fun tagWithImages(tagId: Int) = database.tagDao().getTagWithImagesById(tagId)

    suspend fun updateTag(vararg tags: TagEntity) = database.tagDao().updateTags(*tags)

    suspend fun insertTag(vararg tags: TagEntity) = database.tagDao().insertTags(*tags)

    suspend fun deleteTag(vararg tags: TagEntity) = database.tagDao().deleteTags(*tags)

    suspend fun updateDir(vararg dirs: FolderEntity) = database.folderDao().updateFolders(*dirs)

    suspend fun insertDir(vararg dirs: FolderEntity) = database.folderDao().insertFolders(*dirs)

    val allImages = database.imageDao().getAllImages(20)

    val allDeletedImages = database.imageDao().getAllDeletedImages()

    fun getAllDeletedImagesWithoutFlow() = database.imageDao().getAllDeletedImagesWithoutFlow()

    fun folderWithImages(dirId: Int) = database.folderDao().getFolderWithImagesById(dirId)

    fun childFolder(dirId: Int) = database.folderDao().getChildFolders(dirId)

    fun imageInTag(tagId: Int) = database.tagDao().getTagWithImagesById(tagId)

    fun imageWithTag(imageId: Int) = database.imageDao().getImageWithTagsById(imageId)

    suspend fun removeImagesInRecycleBin(vararg images: ImageEntity) =
        database.imageDao().removeDeletedImages(*images)

    val searchHistories = database.historyDao().getAllHistories()

    suspend fun insertHistories(vararg histories: HistoryEntity) = database.historyDao().insertHistory(*histories)

    suspend fun deleteHistories(vararg histories: HistoryEntity) = database.historyDao().deleteHistory(*histories)
}