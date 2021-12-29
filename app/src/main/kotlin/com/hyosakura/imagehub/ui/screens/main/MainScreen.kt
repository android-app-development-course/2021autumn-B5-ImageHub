package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.runtime.Composable
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.ui.composables.ImageListWithDate

@Composable
fun MainScreen(
    imageList: List<ImageEntity>,
    recentShareImageList: List<ImageEntity>,
    onImageClick: ImageEntity.() -> Unit,
) {
    ImageListWithDate(imageList) {
        onImageClick()
    }
}