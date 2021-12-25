package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.toDateTime
import com.hyosakura.imagehub.ui.screens.Screen
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

private val format = DateTimeFormatter.ofPattern("yyyy/MM/dd")

@Composable
fun ImageList(images: List<ImageEntity>, navController: NavHostController) {
    val map = images.stream().collect(Collectors.groupingBy {
        it.addTime!!.toDateTime().toLocalDate()
    })
    val iterator = map.iterator()
    while (iterator.hasNext()) {
        val entry = iterator.next()
        val date = entry.key
        val list = entry.value
        Column {
            ImageListWithDate(
                date.format(format),
                list.map { it },
                navController
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageListWithDate(date: String, images: List<ImageEntity>, navController: NavHostController) {
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
                ImageItem(image, toDetailScreens = {
                    navController.navigate("${ Screen.Detail.name }/${ image.imageId }")
                })
            }
        }
    }
}

@Composable
private fun ImageItem(image: ImageEntity, toDetailScreens: () -> Unit) {
    Image(
        image.bitmap!!.asImageBitmap(), null,
        Modifier
            .size(120.dp)
            .clickable(onClick = toDetailScreens)
    )
}