package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.entity.relation.DirWithDir
import com.hyosakura.imagehub.entity.relation.DirWithImage

@Dao
interface DirDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDirs(vararg dirs: DirEntity)

    @Query("SELECT * FROM directory")
    fun getAllDirs(): List<DirEntity>

    @Transaction
    @Query("SELECT * FROM directory where dirId = :id")
    fun getChildDirs(id : Int): List<DirWithDir>

    @Transaction
    @Query("SELECT * FROM directory")
    fun getImagesWithImages(): List<DirWithImage>
}