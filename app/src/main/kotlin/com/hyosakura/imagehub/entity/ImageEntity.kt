package com.hyosakura.imagehub.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "image")
data class ImageEntity(
    @field:PrimaryKey(autoGenerate = true)
    override var imageId: Int? = null,

    var folderId: Int = -1,

    override var name: String? = null,

    override var url: String? = null,

    override var ext: String? = null,

    var annotation: String? = "",

    override var width: Int? = null,

    override var height: Int? = null,

    override var size: Double? = null,

    var rating: Int? = null,

    var addTime: Long? = null,

    var shareTime: Long? = null,

    var deleteTime: Long? = null,

    var deleted: Int? = 0,

    @Ignore
    override var bitmap: Bitmap? = null,

    @Ignore
    var thumbnail: Bitmap? = null
) : DeviceImageEntity()

fun Long.toDateTime(): LocalDateTime {
    val instant = Instant.ofEpochMilli(this)
    val zone = ZoneId.systemDefault()
    return LocalDateTime.ofInstant(instant, zone)
}

fun Long.toDate(): LocalDate {
    val instant = Instant.ofEpochMilli(this)
    val zone = ZoneId.systemDefault()
    return instant.atZone(zone).toLocalDate()
}

fun LocalDateTime.toLong(): Long {
    val zone = ZoneId.systemDefault()
    val instant: Instant = this.atZone(zone).toInstant()
    return instant.toEpochMilli()
}

/**
 * 设备图片
 * 其id与子类的id无直接关系
 */
abstract class DeviceImageEntity(
    open var imageId: Int? = null,
    open var name: String? = null,
    open var url: String? = null,
    open var ext: String? = null,
    open var width: Int? = null,
    open var height: Int? = null,
    open var size: Double? = null,
    open var bitmap: Bitmap? = null
)
