package com.hyosakura.imagehub.entity

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "tag")
data class TagEntity(
    @field:PrimaryKey(autoGenerate = true)
    var tagId: Int? = null,
    var name: String? = null,
    var modifyTime: Long? = null
)
