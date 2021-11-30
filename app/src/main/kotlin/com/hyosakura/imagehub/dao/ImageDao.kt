package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.entity.relation.ImageWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(vararg images: ImageEntity)

    @Update
    fun updateImages(vararg images: ImageEntity)

    @Query("SELECT * FROM image WHERE deleted = 0 and annotation like :condition")
    fun searchImage(condition: String): Flow<MutableList<ImageEntity>>

    @Query("SELECT * FROM image where deleted = 0 limit :limit")
    fun getAllImages(limit: Int = 20): Flow<List<ImageEntity>>

    @Query("SELECT * FROM image where imageId = :id")
    fun getImageById(id: Int): ImageEntity

    @Transaction
    @Query("SELECT * FROM image where imageId = :id")
    fun getImageWithTagsById(id: Int): Flow<List<ImageWithTags>>

    @Transaction
    @Query("SELECT * FROM image where name like :name")
    fun getImageWithTagsByName(name: String): Flow<List<ImageWithTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTags(vararg relation: ImageTagCrossRef)
}