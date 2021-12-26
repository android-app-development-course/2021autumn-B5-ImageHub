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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.util.ImageUtil.share
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val coroutine = CoroutineScope(Dispatchers.IO)

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    image: ImageEntity?,
    folder: FolderEntity?,
    tagList: List<TagEntity>?,
    starTags: List<TagEntity>?,
    candidateTags: List<TagEntity>?,
    recentTags: List<TagEntity>?,
    onBack: () -> Unit,
    onTagInsert: suspend TagEntity.() -> Int,
    onTagClick: TagEntity.() -> Unit,
    onTagDelete: TagEntity.() -> Unit,
    onImageDelete: ImageEntity.() -> Unit,
    onImageRecover: ImageEntity.() -> Unit,
    onTagAddToImage: (ImageEntity, TagEntity) -> Unit,
    onFolderClick: FolderEntity.() -> Unit,
    onAnnotationEdit: ImageEntity.(text: String) -> Unit,
    candidateAction:  (String) -> Unit,
    onTagConflict: () -> Unit
) {
    image?.let { i ->
        folder?.let { f ->
            val annotation = i.annotation!!
            var isAnnotationEdit by remember { mutableStateOf(false) }
            var isAddTag by remember { mutableStateOf(false) }

            Scaffold(
                topBar = {
                    SmallTopAppBar(
                        title = {
                            TagRowWithClose(
                                tagList,
                                onTagClick,
                                onTagDelete
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(imageVector = Icons.Filled.ArrowBack, null)
                            }
                        },
                        actions = {
                            IconButton(onClick = { isAddTag = true }) {
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
                                .clickable { isAnnotationEdit = true }
                                .padding(10.dp)) {
                            if (annotation != "")
                                Text(
                                    text = i.annotation!!,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            else
                                Text(
                                    text = stringResource(id = R.string.addAnnotation),
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.alpha(0.5f)
                                )
                        }

                        when (i.deleted) {
                            0 -> {
                                DetailBottomBar(
                                    folderName = f.name,
                                    imageEntity = i,
                                    onDeleteClick = {
                                        onImageDelete(i)
                                    },
                                    onFolderClick = {
                                        onFolderClick(f)
                                    }
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
                                        onClick = {
                                            onImageRecover(i)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            ) {
                Image(
                    bitmap = i.bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    Modifier
                        .fillMaxHeight(0.9f)
                        .fillMaxWidth()
                )

                if (isAnnotationEdit) {
                    var editText by remember { mutableStateOf(annotation) }
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
                                    onAnnotationEdit(i, editText)
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
                    var repeat by remember { mutableStateOf(false) }
                    var tag by remember { mutableStateOf<TagEntity?>(null) }
                    AlertDialog(
                        onDismissRequest = { isAddTag = false },
                        title = { Text(text = "添加标签") },
                        text = {
                            Column {
                                TagRow(
                                    tagList = starTags,
                                    onTagClick = {
                                        onTagClick(it)
                                    }
                                )
                                Row {
                                    OutlinedTextField(
                                        value = editText,
                                        onValueChange = { string ->
                                            editText = string
                                            choose = false
                                            repeat = false
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
                                    candidateAction(editText)
                                    candidateTags?.let{ list ->
                                        list.forEach {
                                            Row {
                                                // todo 改样式
                                                Box(
                                                    modifier = Modifier.clickable {
                                                        if (editText == it.name) {
                                                            repeat = true
                                                        }
                                                        editText = it.name!!
                                                        choose = true
                                                        tag = it
                                                    }
                                                ) {
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
                                            tag!!.tagId = onTagInsert(tag!!)
                                        }
                                        if (!repeat) {
                                            onTagAddToImage(i, tag!!)
                                        } else {
                                           onTagConflict()
                                        }
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
private fun TagRowWithClose(
    tagList: List<TagEntity>?,
    onTagClick: TagEntity.() -> Unit,
    onDeleteClick: TagEntity.() -> Unit
) {
    if (tagList != null) {
        LazyRow {
            items(tagList) { tag ->
                TagItemWithClose(
                    tag,
                    onTagClick = {
                        onTagClick(tag)
                    },
                    onDeleteClick = {
                        onDeleteClick(tag)
                    },
                )
            }
            item { Spacer(modifier = Modifier.width(10.dp)) }
        }
    }
}

@Composable
fun TagItemWithClose(it: TagEntity, onTagClick: () -> Unit, onDeleteClick: () -> Unit) {
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
                .clickable { onDeleteClick() })
    }
}

@Composable
private fun TagRow(
    tagList: List<TagEntity>?,
    onTagClick: (TagEntity) -> Unit,
) {
    if (tagList != null) {
        LazyRow {
            items(tagList) { tag ->
                TagItem(
                    tag,
                    onTagClick = {
                        onTagClick(tag)
                    }
                )
            }
            item { Spacer(modifier = Modifier.width(10.dp)) }
        }
    }
}

@Composable
fun TagItem(it: TagEntity, onTagClick: () -> Unit) {
    Row(Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = onTagClick,
            modifier = Modifier.padding(1.dp)
        ) {
            Text(it.name!!)
        }
    }
}

@Composable
fun DetailBottomBar(
    folderName: String,
    imageEntity: ImageEntity,
    onDeleteClick: () -> Unit,
    onFolderClick: () -> Unit
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