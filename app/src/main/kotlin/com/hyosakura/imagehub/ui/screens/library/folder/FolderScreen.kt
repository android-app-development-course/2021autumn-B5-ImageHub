package com.hyosakura.imagehub.ui.screens.library.folder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.ui.screens.main.ImageListWithDate
import com.hyosakura.imagehub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(
    images: List<ImageEntity>?,
    childFolder: List<FolderEntity>?,
    folder: FolderEntity,
    onBack: () -> Unit,
    onFolderClick: (Int) -> Unit,
    onFolderAdd: (String) -> Unit,
    onImageClick: ImageEntity.() -> Unit
) {
    var isFolderAdd by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text(folder.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isFolderAdd = true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_create_new_folder_24),
                            contentDescription = ""
                        )
                    }
                }
            )
        }) {
        Column(Modifier.fillMaxSize()) {
            FolderList(
                childFolder,
                onFolderClick
            )
            if (!images.isNullOrEmpty()) {
                ImageListWithDate(
                    images = images,
                    onImageClick = onImageClick
                )
            }
        }
    }
}