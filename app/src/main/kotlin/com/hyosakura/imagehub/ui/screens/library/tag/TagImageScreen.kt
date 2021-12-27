package com.hyosakura.imagehub.ui.screens.library.tag

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.ui.screens.main.ImageListWithDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagImageScreen(
    tag: TagEntity?,
    imageList: List<ImageEntity>?,
    onBack: () -> Unit,
    onImageClick: ImageEntity.() -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    tag?.let {
                        Text(it.name!!)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) {
        imageList?.let {
            ImageListWithDate(it, onImageClick)
        }
    }
}