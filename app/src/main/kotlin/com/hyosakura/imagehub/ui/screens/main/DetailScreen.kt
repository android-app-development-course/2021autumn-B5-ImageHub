package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.viewmodel.ImageViewViewModel
import com.hyosakura.imagehub.viewmodel.ImageViewViewModelFactory

@Composable
fun DetailScreen(
    imageId: Int?,
    repository: DataRepository,
    viewModel: ImageViewViewModel = ImageViewViewModelFactory(repository).create(ImageViewViewModel::class.java)
) {
    viewModel.also {
        it.getImageById(imageId!!)
    }.image.observeAsState().value?.let {
        // 读出图片实体
    }
}