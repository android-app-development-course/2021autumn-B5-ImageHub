package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.TagEntity
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

    @Query("SELECT * FROM tag")
    fun getAllTags(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tag where tagId = :id")
    fun getTagById(id: Int): Flow<TagEntity>

    @Query("SELECT * FROM tag where name like :name")
    fun getTagByName(name: String): Flow<List<TagEntity>>

    @Transaction
    @Query("SELECT * FROM tag WHERE name like :condition")
    fun searchImage(condition: String): Flow<MutableList<TagWithImages>>

    @Query("SELECT * FROM tag where star = 1")
    fun starTag(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tag where modifyTime not null order by modifyTime desc limit :limit")
    fun recentTag(limit: Int): Flow<List<TagEntity>>

    @Transaction
    @Query("SELECT * FROM tag where tagId = :id")
    fun getTagWithImagesById(id: Int): Flow<TagWithImages>
}