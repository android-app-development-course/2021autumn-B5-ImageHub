package com.hyosakura.imagehub.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "directory")
data class DirEntity(
    @field:PrimaryKey(autoGenerate = true)
    override var dirId: Int? = null,

    var parentId: Int = -1,

    override var name: String = "未命名",

    @Ignore
    override var url: String? = null,

    var number: Int? = null,

    var modifyTime: Long? = null,

    @Ignore
    override var latestPicture: Bitmap? = null
) : DeviceDirEntity(name = name) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DirEntity

        if (dirId != other.dirId) return false
        if (parentId != other.parentId) return false
        if (name != other.name) return false
        if (number != other.number) return false
        if (modifyTime != other.modifyTime) return false
        if (latestPicture != other.latestPicture) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dirId ?: 0
        result = 31 * result + parentId
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (number ?: 0)
        result = 31 * result + (modifyTime?.hashCode() ?: 0)
        result = 31 * result + (latestPicture?.hashCode() ?: 0)
        return result
    }
}

/**
 * 设备文件夹
 * 其id与子类的id无直接关系
 */
abstract class DeviceDirEntity(
    open var dirId: Int? = null,
    open var name: String,
    open var url: String? = null,
    open var latestPicture: Bitmap? = null
)
