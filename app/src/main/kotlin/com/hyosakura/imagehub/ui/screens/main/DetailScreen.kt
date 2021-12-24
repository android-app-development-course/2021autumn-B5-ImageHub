package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.util.ImageUtil.share
import com.hyosakura.imagehub.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    imageId: Int?,
    repository: DataRepository,
    navController: NavHostController
) {
    val imageManageViewModel: ImageManageViewModel =
        viewModel(factory = ImageManageViewModelFactory(repository))
    val dirViewModel: DirManageViewModel =
        viewModel(factory = DirManageViewModelFactory(repository))
    val tagViewModel: TagManageViewModel =
        viewModel(factory = TagManageViewModelFactory(repository))

    imageManageViewModel.also {
        it.getImageById(imageId!!)
    }.image.observeAsState().value?.let {

        val image = it

        val folder = dirViewModel.visitDir(it.dirId).observeAsState().value

        val labelList = tagViewModel.allTags

        var isAnnotationEdit by remember { mutableStateOf(false) }
        var isAddLabel by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                MediumTopAppBar(
                    // TODO: 显示标签列表
                    title = { Text(text = "这里是标签") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { isAddLabel = true }) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                        }
                    }
                )
            },

            bottomBar = {
                Column {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .alpha(0.8f)
                            .padding(10.dp)
                            .clickable { isAnnotationEdit = true }) {
                        // TODO：修复图片注释 NPE
//                        Text(text = image.annotation!!, style = MaterialTheme.typography.bodySmall)
                    }

                    when (image.deleted) {
                        0 -> {
                            DetailBottomBar(
                                folderName = folder?.name,
                                imageEntity = image,
                                onDeleteClick = {
                                    navController.popBackStack(); it.deleted =
                                    1; imageManageViewModel.updateImage(it)
                                },
                                onFolderClick = { navController.navigate("Folder/${folder?.dirId}") }
                            )
                        }
                        else -> {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            painterResource(id = R.drawable.ic_baseline_restore_from_trash_24),
                                            contentDescription = null
                                        )
                                    },
                                    selected = false,
                                    onClick = {
                                        navController.popBackStack(); it.deleted =
                                        0; imageManageViewModel.updateImage(it)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        ) {
            Image(
                bitmap = image.bitmap!!.asImageBitmap(),
                contentDescription = null,
                Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()
            )

            if (isAnnotationEdit) {
                var editText by remember { mutableStateOf("") }
                AlertDialog(
                    onDismissRequest = { isAnnotationEdit = false },
                    title = { Text(text = "编辑注释") },
                    text = {
                        OutlinedTextField(
                            value = editText, onValueChange = { string ->
                                editText = string
                            },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleMedium,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.inversePrimary,
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                image.annotation = editText
                                imageManageViewModel.updateImage(image)
                                isAnnotationEdit = false
                            }
                        ) {
                            Text("确定")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                isAnnotationEdit = false
                            }
                        ) {
                            Text("取消")
                        }
                    }
                )
            }

            if (isAddLabel) {
                var editText by remember { mutableStateOf("") }
                AlertDialog(
                    onDismissRequest = { isAddLabel = false },
                    title = { Text(text = "添加标签") },
                    text = {
                        OutlinedTextField(
                            value = editText, onValueChange = { string ->
                                editText = string
                            },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleMedium,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.inversePrimary,
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                tagViewModel.insertTag(editText)
                                isAddLabel = false
                            }
                        ) {
                            Text("确定")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                isAddLabel = false
                            }
                        ) {
                            Text("取消")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DetailBottomBar(
    folderName: String?,
    imageEntity: ImageEntity,
    onDeleteClick: () -> Unit,
    onFolderClick: () -> Unit,
) {
    val context = LocalContext.current
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Outlined.Share, null) },
            selected = false,
            label = {
                Text(
                    text = stringResource(R.string.share),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            onClick = {
                context.share(imageEntity.url!!)
            }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_outline_folder_24), null) },
            selected = false,
            label = {
                if (folderName != null) {
                    Text(text = folderName, style = MaterialTheme.typography.labelLarge)
                } else {
                    Text(
                        text = stringResource(R.string.no_folder),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            onClick = onFolderClick
        )

        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Outlined.Delete, null) },
            selected = false,
            label = {
                Text(
                    text = stringResource(R.string.delete),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            onClick = onDeleteClick
        )
    }
}