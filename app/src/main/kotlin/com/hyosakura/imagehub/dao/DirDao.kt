package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.entity.relation.DirWithDir
import com.hyosakura.imagehub.entity.relation.DirWithImage
import kotlinx.coroutines.flow.Flow

@Dao
interface DirDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDirs(vararg dirs: DirEntity)

    @Query("SELECT * FROM directory")
    fun getAllDirs(): Flow<List<DirEntity>>

    @Transaction
    @Query("SELECT * FROM directory WHERE name like :condition")
    fun searchImage(condition: String): Flow<MutableList<DirWithImage>>

    @Transaction
    @Query("SELECT * FROM directory order by modifyTime desc limit :limit")
    fun recentDirWithImages(limit: Int): List<DirWithImage>

    @Transaction
    @Query("SELECT * FROM directory where dirId = :id")
    fun getChildDirs(id : Int): Flow<List<DirWithDir>>

    @Transaction
    @Query("SELECT * FROM directory")
    fun getDirWithImages(): Flow<List<DirWithImage>>
}