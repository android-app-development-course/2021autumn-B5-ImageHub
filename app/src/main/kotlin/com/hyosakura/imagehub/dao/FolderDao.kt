package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.relation.FolderWithFolder
import com.hyosakura.imagehub.entity.relation.FolderWithImage
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolders(vararg dirs: FolderEntity): List<Long>

    @Update
    suspend fun updateFolders(vararg dirs: FolderEntity)

    @Query("SELECT * FROM directory where folderId = :dirId")
    fun getFolderById(dirId: Int): Flow<FolderEntity>

    @Transaction
    @Query("SELECT * FROM directory WHERE name like :condition")
    fun searchImage(condition: String): Flow<MutableList<FolderWithImage>>

    @Transaction
    @Query("SELECT * FROM directory where folderId = :id")
    fun getChildFolders(id: Int): Flow<FolderWithFolder>

    @Transaction
    @Query("SELECT * FROM directory where folderId = :id")
    fun getFolderWithImagesById(id: Int): Flow<FolderWithImage>
}