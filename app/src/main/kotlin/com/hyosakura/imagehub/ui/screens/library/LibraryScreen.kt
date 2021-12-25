package com.hyosakura.imagehub.ui.screens.library

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import com.hyosakura.imagehub.entity.DeviceImageEntity
import com.hyosakura.imagehub.ui.screens.Screen.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(
    navController: NavHostController,
    imageList: Collection<DeviceImageEntity>?
) {
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
                        start.linkTo(parent.start, margin = 4.dp)
                    })
                Button(
                    iconId = R.drawable.ic_outline_folder_24, textId = R.string.folder,
                    onButtonClick = { navController.navigate(Folder.name) },
                    modifier = Modifier.constrainAs(folder) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(label.end, margin = 8.dp)
                        end.linkTo(parent.end, margin = 4.dp)
                    })
                Button(
                    iconId = R.drawable.ic_outline_tip_24, textId = R.string.tip,
                    onButtonClick = { navController.navigate(Tip.name) },
                    modifier = Modifier.constrainAs(tip) {
                        top.linkTo(label.bottom, margin = 16.dp)
                        end.linkTo(trash.start, margin = 8.dp)
                        start.linkTo(parent.start, margin = 4.dp)
                    })
                Button(
                    iconId = R.drawable.ic_baseline_delete_outline_24, textId = R.string.recycleBin,
                    onButtonClick = { navController.navigate(Trash.name) },
                    modifier = Modifier.constrainAs(trash) {
                        top.linkTo(folder.bottom, margin = 16.dp)
                        start.linkTo(tip.end, margin = 8.dp)
                        end.linkTo(parent.end, margin = 4.dp)
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
                    text = stringResource(R.string.picturesInDevice),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(20.dp)
                )
            }

            LazyRow() {
                imageList?.let {
                    val list = it.toList()
                    items(9) { i ->
                        val image = list[i]
                        ImageItem(image) { navController.navigate("${AddDeviceImage.name}/${image.imageId}") }
                    }
                }
            }
        }
    }
}

@Composable
private fun Button(iconId: Int, textId: Int, onButtonClick: () -> Unit, modifier: Modifier) {
    FilledTonalButton(
        onClick = onButtonClick, modifier = modifier
            .height(70.dp)
            .fillMaxWidth(0.5f),
        colors = ButtonDefaults.filledTonalButtonColors(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
                modifier = Modifier.padding(end = 10.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(stringResource(textId), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun ImageItem(image: DeviceImageEntity, onImageClick: () -> Unit) {
    Image(
        bitmap = image.bitmap!!.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier
            .size(120.dp)
            .clickable { onImageClick() }
    )
}