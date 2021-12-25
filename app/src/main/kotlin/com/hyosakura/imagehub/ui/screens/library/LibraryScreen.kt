package com.hyosakura.imagehub.ui.screens.library

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.ui.screens.Screen.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(navController: NavHostController) {

    // TODO: 获取设备上的最近图片，用图片对象保存，该图片对象需要保存到 VM，但不保存到图像表中
    val imageList: MutableList<ImageEntity> = mutableListOf()

    Column {
        // 上半部分
        Column(Modifier.padding(20.dp)) {
            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (label, folder, tip, trash) = createRefs()
                Button(
                    iconId = R.drawable.ic_outline_label_24, textId = R.string.label,
                    onButtonClick = { navController.navigate(Tag.name) },
                    modifier = Modifier.constrainAs(label) {
                        top.linkTo(parent.top, margin = 16.dp)
                        end.linkTo(folder.start, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                    })
                Button(
                    iconId = R.drawable.ic_outline_folder_24, textId = R.string.folder,
                    onButtonClick = { navController.navigate(Folder.name) },
                    modifier = Modifier.constrainAs(folder) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(label.end, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                    })
                Button(
                    iconId = R.drawable.ic_outline_tip_24, textId = R.string.tip,
                    onButtonClick = { navController.navigate(Tip.name) },
                    modifier = Modifier.constrainAs(tip) {
                        top.linkTo(label.bottom, margin = 16.dp)
                        end.linkTo(trash.start, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                    })
                Button(
                    iconId = R.drawable.ic_baseline_delete_outline_24, textId = R.string.recycleBin,
                    onButtonClick = { navController.navigate(Trash.name) },
                    modifier = Modifier.constrainAs(trash) {
                        top.linkTo(folder.bottom, margin = 16.dp)
                        start.linkTo(tip.end, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                    }
                )
            }
        }
        // 下半部分
        Column(Modifier.padding(top = 40.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "设备上的图片",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }

            LazyVerticalGrid(
                cells = GridCells.Adaptive(minSize = 120.dp),
            ) {
                items(imageList) { image ->
                    ImageItem(image) { navController.navigate(AddDeviceImage.name) }
                }
            }
        }
    }
}

@Composable
private fun Button(iconId: Int, textId: Int, onButtonClick: () -> Unit, modifier: Modifier) {
    FilledTonalButton(onClick = onButtonClick, modifier = modifier
        .height(70.dp)
        .width(150.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(stringResource(textId), style = MaterialTheme.typography.titleMedium)

        }
    }
}

@Composable
fun ImageItem(image: ImageEntity, onImageClick: () -> Unit) {
    TextButton(onClick = onImageClick ) {
        Image(
            bitmap = image.bitmap!!.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
        )
    }
}
