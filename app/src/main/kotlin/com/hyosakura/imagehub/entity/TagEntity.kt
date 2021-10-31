package com.hyosakura.imagehub.entity

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4
@Entity(tableName = "tag")
data class TagEntity(
    @field:PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String? = null
)
