package com.hyosakura.imagehub.ui.screens.library

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.DeviceImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.Screen.*
import com.hyosakura.imagehub.viewmodel.DeviceImageViewModel
import com.hyosakura.imagehub.viewmodel.DeviceImageViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val coroutine = CoroutineScope(Dispatchers.IO)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(
    repository: DataRepository,
    navController: NavHostController,
    viewModel: DeviceImageViewModel = viewModel(factory = DeviceImageViewModelFactory(repository))
) {
    LaunchedEffect(LocalContext.current) {
        coroutine.launch {
            viewModel.getDeviceImage()
        }
    }
    val imageList by DeviceImageViewModel.imageList.observeAsState()

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
                imageList?.let {
                    coroutine.launch {
                        items(it.toList()) { image ->
                            ImageItem(image) { navController.navigate("${AddDeviceImage.name}/${image.imageId}") }
                        }
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
            .width(150.dp)
    ) {
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
fun ImageItem(image: DeviceImageEntity, onImageClick: () -> Unit) {
    TextButton(onClick = onImageClick) {
        Image(
            bitmap = image.bitmap!!.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
        )
    }
}
