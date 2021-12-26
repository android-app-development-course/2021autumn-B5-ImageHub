package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.runtime.Composable
import com.hyosakura.imagehub.entity.ImageEntity

@Composable
fun MainScreen(
    list: List<ImageEntity>,
    onImageClick: ImageEntity.() -> Unit,
) {
    ImageList(list) {
        onImageClick()
    }
}