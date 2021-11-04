package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.entity.relation.TagWithImages

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTags(vararg tags: TagEntity)

    @Transaction
    @Query("SELECT * FROM tag where tagId = :id")
    fun getTagWithImagesById(id: Int): List<TagWithImages>

    @Transaction
    @Query("SELECT * FROM tag where name like :name")
    fun getTagWithImagesByName(name: String): List<TagWithImages>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(vararg relation: ImageTagCrossRef)
}