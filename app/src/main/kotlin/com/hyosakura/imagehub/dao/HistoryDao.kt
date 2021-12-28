package com.hyosakura.imagehub.dao

import androidx.room.*
import com.hyosakura.imagehub.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertHistory(vararg histories: HistoryEntity): List<Long>

    @Query("SELECT * FROM search_history order by addTime desc")
    fun getAllHistories(): Flow<List<HistoryEntity>>

    @Delete
    fun deleteHistory(vararg histories: HistoryEntity )
}