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

    @Query("SELECT * FROM image")
    fun getAllImages(): Flow<List<ImageEntity>>

    @Transaction
    @Query("SELECT * FROM image where imageId = :id")
    fun getImageWithTagsById(id: Int): Flow<List<ImageWithTags>>

    @Transaction
    @Query("SELECT * FROM image where name like :name")
    fun getImageWithTagsByName(name: String): Flow<List<ImageWithTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTags(vararg relation: ImageTagCrossRef)
}