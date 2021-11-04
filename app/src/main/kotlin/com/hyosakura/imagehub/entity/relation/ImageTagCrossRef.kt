package com.hyosakura.imagehub.entity.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity

@Entity(primaryKeys = ["imageId", "tagId"])
data class ImageTagCrossRef(
    val imageId: Int,
    val tagId: Int
)

data class ImageWithTags(
    @Embedded var image: ImageEntity,
    @Relation(
        parentColumn = "imageId",
        entityColumn = "tagId",
        associateBy = Junction(ImageTagCrossRef::class)
    )
    var tags: List<TagEntity>
)

data class TagWithImages(
    @Embedded var tag: TagEntity,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "imageId",
        associateBy = Junction(ImageTagCrossRef::class)
    )
    var images: List<ImageEntity>
)
