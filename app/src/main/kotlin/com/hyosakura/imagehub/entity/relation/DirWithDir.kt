package com.hyosakura.imagehub.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.hyosakura.imagehub.entity.DirEntity

data class DirWithDir(
    @field:Embedded val dir: DirEntity,
    @field:Relation(
        parentColumn = "dirId",
        entityColumn = "parentId"
    )
    val childDirs: List<DirEntity>
)
