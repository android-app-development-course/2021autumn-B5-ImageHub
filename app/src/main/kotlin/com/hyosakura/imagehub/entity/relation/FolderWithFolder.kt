package com.hyosakura.imagehub.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.hyosakura.imagehub.entity.FolderEntity

data class FolderWithFolder(
    @field:Embedded val folder: FolderEntity,
    @field:Relation(
        parentColumn = "folderId",
        entityColumn = "parentId"
    )
    val childDirs: List<FolderEntity>
)
