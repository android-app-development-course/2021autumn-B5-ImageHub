package com.hyosakura.imagehub.ui.screens.library.folder

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.FolderEntity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderList(
    childFolder: List<FolderEntity>?,
    onFolderClick: (Int) -> Unit,
) {
    if (!childFolder.isNullOrEmpty()) {
        LazyRow() {
            items(childFolder) { childFolder ->
                Column {
                    TextButton(onClick = { onFolderClick(childFolder.folderId!!) }) {
                        if (childFolder.latestPicture == null) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_folder_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Image(
                                bitmap = childFolder.latestPicture!!.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                            )
                        }
                    }
                    Text(
                        text = childFolder.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 12.dp, top = 8.dp)
                    )
                }
            }
        }
    }
}