package com.hyosakura.imagehub.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "directory")
data class DirEntity(
    @field:PrimaryKey(autoGenerate = true)
    var dirId: Int? = null,
    var parentId: Int? = null,
    var name: String? = null,
    var number: Int? = null,
    var modifyTime: Long? = null,
    var latestPicture: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DirEntity

        if (dirId != other.dirId) return false
        if (parentId != other.parentId) return false
        if (name != other.name) return false
        if (number != other.number) return false
        if (modifyTime != other.modifyTime) return false
        if (latestPicture != null) {
            if (other.latestPicture == null) return false
            if (!latestPicture.contentEquals(other.latestPicture)) return false
        } else if (other.latestPicture != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dirId ?: 0
        result = 31 * result + (parentId ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (number ?: 0)
        result = 31 * result + (modifyTime?.hashCode() ?: 0)
        result = 31 * result + (latestPicture?.contentHashCode() ?: 0)
        return result
    }
}
