package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.Screen
import com.hyosakura.imagehub.util.ImageUtil.share
import com.hyosakura.imagehub.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val coroutine = CoroutineScope(Dispatchers.IO)

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    imageId: Int?,
    repository: DataRepository,
    navController: NavHostController,
    imageViewModel: ImageManageViewModel = viewModel(
        factory = ImageManageViewModelFactory(
            repository
        )
    ),
    dirViewModel: DirManageViewModel = viewModel(factory = DirManageViewModelFactory(repository)),
    tagViewModel: TagManageViewModel = viewModel(factory = TagManageViewModelFactory(repository))
) {
    imageViewModel.also {
        it.visitImage(imageId!!)
    }.image.observeAsState().value?.let { image ->
        dirViewModel.visitDir(image.dirId).observeAsState().value?.let { folder ->
            val tagList by imageViewModel.tagList.observeAsState()
            val annotation = image.annotation!!

            val starTags by tagViewModel.starTags.observeAsState()
            val num = 20
            val recentTags by tagViewModel.getRecentTag(num).observeAsState()

            var isAnnotationEdit by remember { mutableStateOf(false) }
            var isAddTag by remember { mutableStateOf(false) }

            Scaffold(
                topBar = {
                    SmallTopAppBar(
                        title = {
                            TopTagRow(
                                tagList = tagList,
                                onTagClick = onTagJumpClick(navController),
                                onDeleteTagClick = { tagEntity: TagEntity ->
                                    imageViewModel.removeTag(image, tagEntity)
                                },
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(imageVector = Icons.Filled.ArrowBack, null)
                            }
                        },
                        actions = {
                            IconButton(onClick = { isAddTag = true }) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    )
                },

                bottomBar = {
                    Column {
                        Annotation(
                            onAnnotationEdit = { isAnnotationEdit = true },
                            annotation = annotation
                        )
                        DetailBottomBar(image, folder.name,
                            onDeleteImageClick = {
                                navController.popBackStack()
                                image.deleted = 1
                                imageViewModel.updateImage(image)
                            },
                            onFolderClick = { navController.navigate("Folder/${folder.dirId}") },
                            onRestoreClick = {
                                navController.popBackStack()
                                image.deleted = 0
                                imageViewModel.updateImage(image)
                            }
                        )
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
                    var editText by remember { mutableStateOf(annotation) }
                    AlertDialog(
                        onDismissRequest = { isAnnotationEdit = false },
                        title = { Text(text = stringResource(R.string.editAnnotation)) },
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
                                    imageViewModel.updateImage(image)
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

                if (isAddTag) {
                    var editText by remember { mutableStateOf("") }
                    var choose by remember { mutableStateOf(false) }
                    var tag by remember { mutableStateOf<TagEntity?>(null) }
                    AlertDialog(
                        onDismissRequest = { isAddTag = false },
                        title = { Text(text = stringResource(R.string.addTag)) },
                        text = {
                            Column {
                                Text(text = stringResource(R.string.starTags))
                                TagRow(
                                    tagList = starTags,
                                    onSuggestTagClick = { tagEntity ->
                                        onSuggestTagClick(
                                            image,
                                            tagEntity,
                                            imageViewModel
                                        )
                                        isAddTag = false
                                    }
                                )
                                Text(text = stringResource(R.string.recentlyUsed))
                                TagRow(
                                    tagList = recentTags,
                                    onSuggestTagClick = { tagEntity ->
                                        onSuggestTagClick(
                                            image,
                                            tagEntity,
                                            imageViewModel
                                        )
                                        isAddTag = false
                                    }
                                )
                                Text(text = stringResource(R.string.addOrSearchTags))
                                Row {
                                    OutlinedTextField(
                                        value = editText,
                                        onValueChange = { string ->
                                            editText = string
                                            choose = false
                                        },
                                        singleLine = true,
                                        textStyle = MaterialTheme.typography.titleMedium,
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            textColor = MaterialTheme.colorScheme.primary,
                                            cursorColor = MaterialTheme.colorScheme.inversePrimary,
                                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
                                        ),
                                    )
                                }
                                if (editText.isNotEmpty() && !choose) {
                                    tagViewModel.getTagByName(editText)
                                        .observeAsState().value?.let { list ->
                                            list.forEach {
                                                Row {
                                                    // todo 改样式
                                                    Box(modifier = Modifier.clickable {
                                                        editText = it.name!!
                                                        choose = true
                                                        tag = it
                                                    }) {
                                                        Text(it.name!!)
                                                    }
                                                }
                                            }
                                        }
                                }
                            }

                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    coroutine.launch {
                                        if (!choose) {
                                            tag = TagEntity(name = editText)
                                            tag!!.tagId =
                                                tagViewModel.insertTagAndGetId(tag!!).first()
                                                    .toInt()
                                        }
                                        imageViewModel.addTagToImage(image, tag!!)
                                        isAddTag = false
                                    }
                                }
                            ) {
                                Text("确定")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    isAddTag = false
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
}

@Composable
private fun TopTagRow(
    tagList: List<TagEntity>?,
    onTagClick: (TagEntity) -> Unit,
    onDeleteTagClick: (TagEntity) -> Unit
) {
    if (tagList?.isEmpty() == true) {
        Text(
            text = stringResource(R.string.clickToAddTag),
            modifier = Modifier.alpha(0.5f),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )
    } else
        TagRowWithClose(
            tagList,
            onTagClick = onTagClick,
            onDeleteTagClick = { tag: TagEntity -> onDeleteTagClick(tag) }
        )
}

@Composable
private fun TagRowWithClose(
    tagList: List<TagEntity>?,
    onTagClick: (TagEntity) -> Unit,
    onDeleteTagClick: (TagEntity) -> Unit
) {
    if (tagList != null) {
        LazyRow {
            items(tagList) { tag ->
                TagItemWithClose(
                    tag,
                    onTagClick = {
                        onTagClick(tag)
                    },
                    onDeleteTagClick = {
                        onDeleteTagClick(tag)
                    },
                )
            }
            item { Spacer(modifier = Modifier.width(10.dp)) }
        }
    }
}


@Composable
fun TagItemWithClose(it: TagEntity, onTagClick: () -> Unit, onDeleteTagClick: () -> Unit) {
    Row(Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = onTagClick,
            modifier = Modifier.padding(1.dp)
        ) {
            Text(it.name!!, Modifier.offset(x = (-10).dp))
        }
        Icon(imageVector = Icons.Filled.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .offset(x = (-30).dp)
                .clickable { onDeleteTagClick() })
    }
}

@Composable
private fun TagRow(
    tagList: List<TagEntity>?,
    onSuggestTagClick: (TagEntity) -> Unit,
) {
    if (tagList != null) {
        LazyRow {
            items(tagList) { tag ->
                TagItem(
                    tagEntity = tag,
                    onTagClick = {
                        onSuggestTagClick(tag)
                    }
                )
            }
            item { Spacer(modifier = Modifier.width(10.dp)) }
        }
    }
}

@Composable
fun TagItem(tagEntity: TagEntity, onTagClick: () -> Unit) {
    Row(Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = onTagClick,
            modifier = Modifier.padding(1.dp),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text(tagEntity.name!!)
        }
    }
}


@Composable
private fun Annotation(
    onAnnotationEdit: () -> Unit,
    annotation: String
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .alpha(0.8f)
            .clickable { onAnnotationEdit() }
            .padding(10.dp)) {
        if (annotation != "")
            Text(
                text = annotation,
                style = MaterialTheme.typography.bodySmall
            )
        else
            Text(
                text = stringResource(id = R.string.addAnnotation),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.alpha(0.5f)
            )
    }
}

@Composable
private fun DetailBottomBar(
    image: ImageEntity,
    folderName: String,
    onDeleteImageClick: () -> Unit,
    onFolderClick: () -> Unit,
    onRestoreClick: () -> Unit,
) {
    when (image.deleted) {
        0 -> {
            BottomBar(
                folderName = folderName,
                imageEntity = image,
                onDeleteClick = onDeleteImageClick,
                onFolderClick = onFolderClick,
            )
        }
        else -> {
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
                    label = { Text(text = stringResource(R.string.restore)) },
                    onClick = onRestoreClick
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    folderName: String,
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
                context.share(imageEntity.bitmap!!)
            }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_outline_folder_24), null) },
            selected = false,
            label = {
                Text(text = folderName, style = MaterialTheme.typography.labelLarge)
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

private fun onSuggestTagClick(
    image: ImageEntity,
    tag: TagEntity,
    imageViewModel: ImageManageViewModel
) {
    coroutine.launch {
        imageViewModel.addTagToImage(image, tag)
    }
}

private fun onTagJumpClick(navController: NavHostController) = { tagEntity: TagEntity ->
    navController.navigate("${Screen.TagImage.name}/${tagEntity.tagId}")
}