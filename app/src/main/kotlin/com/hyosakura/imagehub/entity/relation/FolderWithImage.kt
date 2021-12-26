package com.hyosakura.imagehub.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity

data class FolderWithImage(
    @field:Embedded val folder: FolderEntity,
    @field:Relation(
        parentColumn = "folderId",
        entityColumn = "folderId"
    )
    val images: List<ImageEntity>
)
