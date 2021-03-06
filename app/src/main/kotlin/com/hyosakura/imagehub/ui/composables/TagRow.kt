package com.hyosakura.imagehub.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.entity.TagEntity

@Composable
fun TagRow(
    tagList: List<TagEntity>,
    onSuggestTagClick: (TagEntity) -> Unit,
) {
    LazyRow(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
        items(tagList) { tag ->
            if (tag.latestPicture != null) {
                TagItem(
                    tagEntity = tag,
                    onTagClick = {
                        onSuggestTagClick(tag)
                    }
                )
            }
        }
        item { Spacer(modifier = Modifier.width(10.dp)) }
    }
}

@Composable
fun TagItem(
    tagEntity: TagEntity,
    onTagClick: () -> Unit
) {
    Column {
        TextButton(onClick = onTagClick) {
            Image(
                bitmap = tagEntity.latestPicture!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
            )
        }
        Text(
            text = tagEntity.name!!,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 12.dp, top = 8.dp)
        )
    }
}
