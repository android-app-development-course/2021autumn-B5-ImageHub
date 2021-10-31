package com.hyosakura.imagehub.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
data class ImageEntity(
    @field:PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String? = null,
    var url: String? = null,
    var ext: String? = null,
    var annotation: String? = null,
    var width: Int? = null,
    var height: Int? = null,
    var size: Double? = null,
    var rating: Int? = null
)
