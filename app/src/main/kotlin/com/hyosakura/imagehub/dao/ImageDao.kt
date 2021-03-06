package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.entity.relation.ImageWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(vararg images: ImageEntity): List<Long>

    @Update
    suspend fun updateImages(vararg images: ImageEntity)

    @Query("SELECT * FROM image WHERE deleted = 0 and annotation like :condition")
    fun searchImage(condition: String): Flow<MutableList<ImageEntity>>

    @Query("SELECT * FROM image where deleted = 0 limit :limit")
    fun getAllImages(limit: Int = 20): Flow<List<ImageEntity>>

    @Query("SELECT * FROM image where shareTime not null order by shareTime desc limit :limit")
    fun getRecentShareImages(limit: Int = 10): Flow<List<ImageEntity>>

    @Query("SELECT * FROM image where deleted = 1")
    fun getAllDeletedImages(): Flow<List<ImageEntity>>

    @Query("SELECT * FROM image where imageId = :id")
    fun getImageById(id: Int): Flow<ImageEntity>

    @Transaction
    @Query("SELECT * FROM image where imageId = :id")
    fun getImageWithTagsById(id: Int): Flow<ImageWithTags>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(vararg relation: ImageTagCrossRef)

    @Delete
    suspend fun removeTags(vararg relation: ImageTagCrossRef)

    @Delete
    suspend fun removeDeletedImages(vararg images: ImageEntity)
}