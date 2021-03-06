package com.hyosakura.imagehub.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "directory")
data class FolderEntity(
    @field:PrimaryKey(autoGenerate = true)
    override var folderId: Int? = null,

    var parentId: Int = -1,

    override var name: String = "",

    @Ignore
    override var url: String? = null,

    var number: Int? = null,

    var modifyTime: Long? = null,

    @Ignore
    override var latestPicture: Bitmap? = null
) : DeviceFolderEntity(name = name) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FolderEntity

        if (folderId != other.folderId) return false
        if (parentId != other.parentId) return false
        if (name != other.name) return false
        if (url != other.url) return false
        if (number != other.number) return false
        if (modifyTime != other.modifyTime) return false
        if (latestPicture != other.latestPicture) return false

        return true
    }

    override fun hashCode(): Int {
        var result = folderId ?: 0
        result = 31 * result + parentId
        result = 31 * result + name.hashCode()
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
abstract class DeviceFolderEntity(
    open var folderId: Int? = null,
    open var name: String,
    open var url: String? = null,
    open var latestPicture: Bitmap? = null
)
