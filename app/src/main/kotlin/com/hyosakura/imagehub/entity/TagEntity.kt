package com.hyosakura.imagehub.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "tag")
data class TagEntity(
    @field:PrimaryKey(autoGenerate = true)
    var tagId: Int? = null,

    var name: String? = null,

    var star: Int = 0,

    var addTime: Long? = null,

    var modifyTime: Long? = null,

    @Ignore
    var latestPicture: Bitmap? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TagEntity

        if (tagId != other.tagId) return false
        if (name != other.name) return false
        if (star != other.star) return false
        if (addTime != other.addTime) return false
        if (modifyTime != other.modifyTime) return false
        if (latestPicture != other.latestPicture) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tagId ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + star
        result = 31 * result + (addTime?.hashCode() ?: 0)
        result = 31 * result + (modifyTime?.hashCode() ?: 0)
        result = 31 * result + (latestPicture?.hashCode() ?: 0)
        return result
    }
}
