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

    @Query("SELECT * FROM image where deleted = 0 limit :limit")
    fun getAllImages(limit: Int = 20): Flow<List<ImageEntity>>

    @Query("SELECT * FROM image where deleted = 0 and shareTime between :from and :to order by shareTime desc")
    fun shareImageListBetweenDate(from: Long, to: Long): Flow<List<ImageEntity>>

    @Query("SELECT addTime FROM image where deleted = 0 and name like :key")
    fun addDateOfSearchImage(key: String): Flow<List<ImageEntity>>

    @Query("SELECT * FROM image where deleted = 0 and name like :key and shareTime between :from and :to")
    fun searchImageBetweenDate(key: String, from: Long, to: Long): Flow<List<ImageEntity>>

    @Query("SELECT * FROM image where deleted = 0 order by shareTime desc limit :limit")
    fun recentShareImage(limit: Int = 20): Flow<List<ImageEntity>>

    @Query("SELECT addTime FROM image where deleted = 0 order by addTime desc limit :limit")
    fun addDateOfAddImage(limit: Int = 20): Flow<List<ImageEntity>>

    @Query("SELECT addTime FROM image where deleted = 0 and addTime between :from and :to order by addTime desc limit :limit")
    fun addImageListBetweenDate(from: Long, to: Long, limit: Int = 20): Flow<List<ImageEntity>>



    @Transaction
    @Query("SELECT * FROM image where imageId = :id")
    fun getImageWithTagsById(id: Int): Flow<List<ImageWithTags>>

    @Transaction
    @Query("SELECT * FROM image where name like :name")
    fun getImageWithTagsByName(name: String): Flow<List<ImageWithTags>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTags(vararg relation: ImageTagCrossRef)
}