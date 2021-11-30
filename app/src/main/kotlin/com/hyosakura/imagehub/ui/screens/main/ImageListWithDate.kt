package com.hyosakura.imagehub.ui.screens.main

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun ImageListWithDate(date: String, images: List<Bitmap>) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth().padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        ImageGrid(images)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageGrid(images: List<Bitmap>) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 120.dp),
    ) {
        items(images.size) { image ->
            ImageItem(images, image)
        }
    }
}

@Composable
private fun ImageItem(images: List<Bitmap>, image: Int) {
    Image(
        images[image].asImageBitmap(), null,
        Modifier
            .size(120.dp)
            .clickable { /*TODO*/ }
    )
}


@Preview
@Composable
fun PictureListWithDatePreview() {
    ImageListWithDate(
        "2020/01/01", listOf(
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            )
    )
}