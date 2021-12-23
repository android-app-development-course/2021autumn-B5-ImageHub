package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailScreen(imageId: Int?) {
    Text(text = imageId.toString())
}