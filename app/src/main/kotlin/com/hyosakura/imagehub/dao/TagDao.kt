package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.entity.relation.TagWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(vararg tags: TagEntity): List<Long>

    @Update
    suspend fun updateTags(vararg tags: TagEntity)

    @Delete
    suspend fun deleteTags(vararg tags: TagEntity)

    @Query("SELECT * FROM tag limit :limit")
    fun getAllTags(limit: Int = 20): Flow<List<TagEntity>>

    @Query("SELECT * FROM tag where name like :name")
    fun getTagByName(name: String): Flow<List<TagEntity>>

    @Query("SELECT * FROM tag where name like :name")
    suspend fun getTagByNameWithOutFlow(name: String): List<TagEntity>

    @Transaction
    @Query("SELECT * FROM tag WHERE name like :condition")
    fun searchImage(condition: String): Flow<MutableList<TagWithImages>>

    @Query("SELECT * FROM tag where star = 1")
    fun starTag(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tag order by modifyTime desc limit :limit")
    fun recentTag(limit: Int): Flow<List<TagEntity>>

    @Transaction
    @Query("SELECT * FROM tag where tagId = :id")
    fun getTagWithImagesById(id: Int): Flow<TagWithImages>

    @Transaction
    @Query("SELECT * FROM tag where name like :name")
    fun getTagWithImagesByName(name: String): Flow<TagWithImages>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(vararg relation: ImageTagCrossRef)
}