package com.hyosakura.imagehub.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class HistoryEntity(
    @field:PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    var keyword: String? = null,

    var addTime: Long? = null
)
