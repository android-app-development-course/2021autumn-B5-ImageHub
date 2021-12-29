package com.hyosakura.imagehub.ui.screens.library.trash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.ui.composables.ImageListWithDate
import com.hyosakura.imagehub.viewmodel.RecycleBinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(
    onBack: () -> Unit,
    viewModel: RecycleBinViewModel,
    deletedImages: List<ImageEntity>?,
    onImageClick: ImageEntity.() -> Unit
) {
    Scaffold(topBar = {
        SmallTopAppBar(
            title = { Text("回收站") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            }
        )
    }) {
        Column {
            Text(
                text = stringResource(R.string.trashDeleteRule),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(20.dp)
            )
            deletedImages?.let {
                ImageListWithDate(it, onImageClick)
            }
        }
    }
}