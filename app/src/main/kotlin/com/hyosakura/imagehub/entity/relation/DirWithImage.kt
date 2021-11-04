package com.hyosakura.imagehub.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.entity.ImageEntity

data class DirWithImage(
    @field:Embedded val dir: DirEntity,
    @field:Relation(
        parentColumn = "dirId",
        entityColumn = "dirId"
    )
    val images: List<ImageEntity>
)
