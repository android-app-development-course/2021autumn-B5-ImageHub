package com.hyosakura.imagehub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.TagEntity

@Composable
fun TagRow(
    tagList: List<TagEntity>?,
    onSuggestTagClick: (TagEntity) -> Unit,
) {
    if (tagList != null) {
        LazyRow(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
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

@Composable
fun TagItem(tagEntity: TagEntity, onTagClick: () -> Unit) {
    Column {
        TextButton(onClick = onTagClick) {
            Image(
                // TODO: 获取标签最新图片放在下面
//                bitmap = tagEntity.latestPicture,
                painter = painterResource(id = R.drawable.ic_outline_image_black_24),
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
