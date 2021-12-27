package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.relation.FolderWithFolder
import com.hyosakura.imagehub.entity.relation.FolderWithImage
import kotlinx.coroutines.flow.Flow

@Dao
interface DirDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirs(vararg dirs: FolderEntity): List<Long>

    @Update
    suspend fun updateDirs(vararg dirs: FolderEntity)

    @Query("SELECT * FROM directory")
    fun getAllDirs(): Flow<List<FolderEntity>>

    @Query("SELECT * FROM directory where folderId = :dirId")
    fun getDirById(dirId: Int): Flow<FolderEntity>

    @Transaction
    @Query("SELECT * FROM directory WHERE name like :condition")
    fun searchImage(condition: String): Flow<MutableList<FolderWithImage>>

    @Transaction
    @Query("SELECT * FROM directory where folderId = :id")
    fun getChildDirs(id: Int): Flow<FolderWithFolder>

    @Transaction
    @Query("SELECT * FROM directory where folderId = :id")
    fun getDirWithImagesById(id: Int): Flow<FolderWithImage>

    @Transaction
    @Query("SELECT * FROM directory where name like :name")
    fun getDirWithImagesByName(name: String): Flow<FolderWithImage>
}