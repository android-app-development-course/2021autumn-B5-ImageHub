package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.entity.relation.TagWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTags(vararg tags: TagEntity)

    @Query("SELECT * FROM tag")
    fun getAllTags(): Flow<List<TagEntity>>

    @Transaction
    @Query("SELECT * FROM tag where tagId = :id")
    fun getTagWithImagesById(id: Int): Flow<List<TagWithImages>>

    @Transaction
    @Query("SELECT * FROM tag where name like :name")
    fun getTagWithImagesByName(name: String): Flow<List<TagWithImages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(vararg relation: ImageTagCrossRef)
}