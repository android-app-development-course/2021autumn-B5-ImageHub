package com.hyosakura.imagehub.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.toDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

private val format = DateTimeFormatter.ofPattern("yyyy/MM/dd")

@Composable
fun ImageListWithDate(
    images: List<ImageEntity>,
    onImageClick: ImageEntity.() -> Unit
) {
    val map = images.stream().collect(Collectors.groupingBy {
        it.addTime!!.toDateTime().toLocalDate()
    })
    val iterator = map.iterator()
    while (iterator.hasNext()) {
        val entry = iterator.next()
        val date = entry.key
        val list = entry.value
        Column {
            ImageRowWithDate(
                date.format(format),
                list.map { it },
                onImageClick
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageRowWithDate(
    date: String,
    images: List<ImageEntity>,
    onImageClick: ImageEntity.() -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 120.dp),
        ) {
            items(images) { image ->
                ImageItem(
                    image,
                    onImageClick
                )
            }
        }
    }
}

@Composable
fun ImageItem(
    image: ImageEntity,
    onImageClick: ImageEntity.() -> Unit
) {
    Box(
        modifier = Modifier.clickable {
            onImageClick(image)
        }
    ) {
        Image(
            bitmap = image.thumbnail!!.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageList(
    images: List<ImageEntity>,
    onImageClick: ImageEntity.() -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 120.dp),
    ) {
        items(images) { image ->
            ImageItem(
                image,
                onImageClick
            )
        }
    }
}