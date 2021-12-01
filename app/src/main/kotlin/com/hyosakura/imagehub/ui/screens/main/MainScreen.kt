package com.hyosakura.imagehub.ui.screens.main

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    val images = mutableListOf(1, 2, 3, 4, 5,6,7,8,9,10,11,12,13,14,15)

    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 120.dp),
    ) {
        items(images.size) { image ->
            ImageItem(images, image)
        }
    }
}

//        Column {
//            val map = vm.fakeImageIdList()
//            for (key in map.keys) {
//                val idMap = mutableMapOf<Int, Bitmap>()
//                map[key]?.let {
//                    for (id in it) {
//                        idMap[id] = vm.fakeGetBitMapById(id)
//                    }
//                }
//                ImageListWithDate(date = key, images = idMap)
//            }
//        }


@Composable
fun ImageItem(images: List<Int>, image: Int) {
    TextButton(onClick = { /*TODO*/ }) {
        Image(
            painter = painterResource(R.drawable.ic_outline_image_black_24),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
        )
    }
}
