package com.hyosakura.imagehub.ui.screens.library.folder

import androidx.compose.runtime.Composable
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity

@Composable
fun FolderScreen(
    images: List<ImageEntity>?,
    childFolder: List<FolderEntity>?
) {
    FolderList(
        images,
        childFolder
    )
}