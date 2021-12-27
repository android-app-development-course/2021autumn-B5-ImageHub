package com.hyosakura.imagehub.ui.screens.library.folder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.ui.screens.main.ImageListWithDate
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.ui.composables.InputOutlinedTextField

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
                title = { Text(text = if(folder.name != "无文件夹") folder.name else "\\") },
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

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val fab = createRef()
            ExtendedFloatingActionButton(
                onClick = {
                    isFolderAdd = true
                },
                icon = { Icon(Icons.Filled.Add, "添加文件夹") },
                text = { Text(text = stringResource(id = R.string.addFolder)) },
                modifier = Modifier.constrainAs(fab) {
                    bottom.linkTo(parent.bottom, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    start.linkTo(parent.start, 16.dp)
                }
            )
        }

        if (isFolderAdd) {
            var editText by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { isFolderAdd = false },
                title = { Text(text = stringResource(R.string.addFolder)) },
                text = {
                    Row {
                        InputOutlinedTextField(
                            value = editText,
                            onValueChange = { string ->
                                editText = string
                            }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isFolderAdd = false
                            onFolderAdd(editText)
                        },
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            isFolderAdd = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}