package com.hyosakura.imagehub.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "image")
data class ImageEntity(
    @field:PrimaryKey(autoGenerate = true)
    override var imageId: Int? = null,

    var dirId: Int? = null,

    override var name: String? = null,

    override var url: String? = null,

    override var ext: String? = null,

    var annotation: String? = null,

    override var width: Int? = null,

    override var height: Int? = null,

    override var size: Double? = null,

    var rating: Int? = null,

    var addTime: Long? = null,

    var shareTime: Long? = null,

    var deleted: Int? = 0,

    @Ignore
    var bitmap: Bitmap? = null
) : DeviceImageEntity()

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
)
