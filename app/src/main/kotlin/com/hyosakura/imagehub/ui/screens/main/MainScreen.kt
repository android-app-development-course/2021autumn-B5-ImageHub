package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.ui.composables.ImageListWithDate
import com.hyosakura.imagehub.ui.composables.ImageRow

@Composable
fun MainScreen(
    imageList: List<ImageEntity>,
    recentShareImageList: List<ImageEntity>,
    onImageClick: ImageEntity.() -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.recentShare),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 15.dp)
        )
        ImageRow (images = recentShareImageList, onImageClick = onImageClick)
        Spacer(modifier = Modifier.height(20.dp))
        ImageListWithDate(images = imageList, onImageClick = onImageClick)
    }
}