package com.hyosakura.imagehub.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag")
data class TagEntity(
    @field:PrimaryKey(autoGenerate = true)
    var tagId: Int? = null,

    var name: String? = null,

    var star: Int = 0,

    var addTime: Long? = null,

    var modifyTime: Long? = null,

    var latestPicture: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TagEntity

        if (tagId != other.tagId) return false
        if (name != other.name) return false
        if (modifyTime != other.modifyTime) return false
        if (latestPicture != null) {
            if (other.latestPicture == null) return false
            if (!latestPicture.contentEquals(other.latestPicture)) return false
        } else if (other.latestPicture != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tagId ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (modifyTime?.hashCode() ?: 0)
        result = 31 * result + (latestPicture?.contentHashCode() ?: 0)
        return result
    }
}
