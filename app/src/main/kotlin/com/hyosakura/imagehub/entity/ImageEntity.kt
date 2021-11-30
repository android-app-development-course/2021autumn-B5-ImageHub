package com.hyosakura.imagehub.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "image")
data class ImageEntity(
    @field:PrimaryKey(autoGenerate = true)
    var imageId: Int? = null,
    var dirId: Int? = null,
    var name: String? = null,
    var url: String? = null,
    var ext: String? = null,
    var annotation: String? = null,
    var width: Int? = null,
    var height: Int? = null,
    var size: Double? = null,
    var rating: Int? = null,
    var addTime: Long? = null,
    var shareTime: Long? = null,
    var deleted: Int? = 0
)

fun Long.toDate(): LocalDateTime {
    val instant = Instant.ofEpochMilli(this)
    val zone = ZoneId.systemDefault()
    return LocalDateTime.ofInstant(instant, zone)
}

fun LocalDateTime.toLong(): Long {
    val zone = ZoneId.systemDefault()
    val instant: Instant = this.atZone(zone).toInstant()
    return instant.toEpochMilli()
}
