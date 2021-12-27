package com.hyosakura.imagehub.ui.screens.library.folder


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.ui.composables.ImageListWithDate
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.ui.composables.InputOutlinedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderChooseScreen(
    images: List<ImageEntity>?,
    childFolder: List<FolderEntity>?,
    folder: FolderEntity,
    onBack: () -> Unit,
    onFolderClick: (Int) -> Unit,
    onFolderAdd: (String) -> Unit,
    onChooseClick: (FolderEntity) -> Unit
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
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(
                                id = R.drawable.ic_baseline_restore_from_trash_24
                            ),
                            contentDescription = null
                        )
                    },
                    selected = false,
                    label = { Text(text = stringResource(R.string.moveToThisFolder)) },
                    onClick = { onChooseClick(folder) }
                )
            }
        }
    ) {
        Column(Modifier.fillMaxSize()) {
            FolderList(
                childFolder,
                onFolderClick
            )
            if (!images.isNullOrEmpty()) {
                ImageListWithDate(
                    images = images,
                    onImageClick = { }
                )
            }
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