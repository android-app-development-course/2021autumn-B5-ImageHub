package com.hyosakura.imagehub.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "directory")
data class DirEntity(
    @field:PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String? = null,
    var number: Int? = null,
)
