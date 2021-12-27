package com.hyosakura.imagehub.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.entity.TagEntity

@Composable
fun TagRow(
    tagList: List<TagEntity>?,
    onSuggestTagClick: (TagEntity) -> Unit,
) {
    if (tagList != null) {
        LazyRow {
            items(tagList) { tag ->
                TagItem(
                    tagEntity = tag,
                    onTagClick = {
                        onSuggestTagClick(tag)
                    }
                )
            }
            item { Spacer(modifier = Modifier.width(10.dp)) }
        }
    }
}