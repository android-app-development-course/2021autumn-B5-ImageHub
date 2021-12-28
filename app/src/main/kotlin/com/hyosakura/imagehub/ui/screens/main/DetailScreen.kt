package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.ui.composables.InputOutlinedTextField
import com.hyosakura.imagehub.ui.composables.MiniTagRow
import com.hyosakura.imagehub.util.ImageUtil.share
import com.hyosakura.imagehub.viewmodel.ImageManageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val coroutine = CoroutineScope(Dispatchers.IO)

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    image: ImageEntity,
    folderName: String,
    tagList: List<TagEntity>,
    starTagList: List<TagEntity>,
    recentTagList: List<TagEntity>,
    candidateTagList: List<TagEntity>,

    onBack: () -> Unit,

    onTagInsert: suspend TagEntity.() -> Int,
    onTagClick: TagEntity.() -> Unit,
    onTagDelete: TagEntity.() -> Unit,
    onTagAddToImage: (TagEntity) -> Unit,
    onTagConflict: () -> Unit,
    candidateAction: (String) -> Unit,

    onImageDelete: () -> Unit,
    onImageRestore: () -> Unit,

    onFolderClick: () -> Unit,

    onAnnotationEdit: (text: String) -> Unit
) {

    val annotation = image.annotation!!

    var isAnnotationEdit by remember { mutableStateOf(false) }
    var isAddTag by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    TopTagRow(
                        tagList = tagList,
                        onTagClick = onTagClick,
                        onDeleteTagClick = onTagDelete,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                DetailBottomBar(image, folderName,
                    onDeleteImageClick = { onImageDelete() },
                    onFolderClick = { onFolderClick() },
                    onRestoreClick = { onImageRestore() }
                )
            }
        }
    ) {
        image.bitmap?.let { it1 ->
            Image(
                bitmap = ImageManageViewModel.idToBitMap[image.imageId]!!.asImageBitmap(),
                contentDescription = null,
                Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()
            )
        }

        if (isAnnotationEdit) {
            var editText by remember { mutableStateOf(annotation) }
            AlertDialog(
                onDismissRequest = { isAnnotationEdit = false },
                title = { Text(text = "编辑注释") },
                text = {
                    InputOutlinedTextField(
                        value = editText,
                        onValueChange = { string ->
                            editText = string
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isAnnotationEdit = false
                            onAnnotationEdit(editText)
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
            var candidateChoosed by remember { mutableStateOf(false) }
            var tag by remember { mutableStateOf<TagEntity?>(null) }
            AlertDialog(
                onDismissRequest = { isAddTag = false },
                title = { Text(text = stringResource(R.string.addTag)) },
                text = {
                    Column {
                        val mostUse = starTagList - tagList
                        if (mostUse.isNotEmpty()) {
                            Text(text = stringResource(R.string.starTags))
                            MiniTagRow(
                                tagList = mostUse,
                                onSuggestTagClick = {
                                    isAddTag = false
                                    onTagAddToImage(it)
                                }
                            )
                        }
                        if (editText.isBlank() || (candidateTagList - tagList).isEmpty()) {
                            Text(text = stringResource(R.string.recentlyUsed))
                            MiniTagRow(
                                tagList = recentTagList - tagList,
                                onSuggestTagClick = {
                                    isAddTag = false
                                    onTagAddToImage(it)
                                }
                            )
                        } else {
                            if ((candidateTagList - tagList).isNotEmpty()) {
                                Text(text = stringResource(R.string.suggestTag))
                                MiniTagRow(
                                    tagList = candidateTagList - tagList,
                                    onSuggestTagClick = {
                                        editText = it.name!!
                                        // 选择了建议标签
                                        candidateChoosed = true
                                        // 将建议标签给结果
                                        tag = it
                                        isAddTag = false
                                        onTagAddToImage(it)
                                    }
                                )
                            }
                        }
                        Text(text = stringResource(R.string.addOrSearchTags))
                        Row {
                            InputOutlinedTextField(
                                value = editText,
                                onValueChange = { string ->
                                    editText = string
                                    candidateAction(editText)
                                    // 重置状态
                                    candidateChoosed = false
                                }
                            )
                        }
                    }

                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutine.launch {
                                // 没选择建议标签
                                if (!candidateChoosed) {
                                    // 没选择但数据库中有
                                    val filterList = candidateTagList.filter {
                                        it.name == editText
                                    }
                                    if (filterList.isNotEmpty()) {
                                        tag = filterList[0]
                                    } else {
                                        // 没有选择,数据库中也没有，则新建
                                        tag = TagEntity(
                                            name = editText,
                                            addTime = System.currentTimeMillis()
                                        )
                                        tag!!.tagId = onTagInsert(tag!!)
                                    }
                                }
                                // 查看是否重复添加标签
                                if (
                                    tagList.any {
                                        it.name == editText
                                    }
                                ) {
                                    onTagConflict()
                                } else {
                                    onTagAddToImage(tag!!)
                                }
                                isAddTag = false
                            }
                        }
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            isAddTag = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}

@Composable
private fun TopTagRow(
    tagList: List<TagEntity>,
    onTagClick: TagEntity.() -> Unit,
    onDeleteTagClick: (TagEntity) -> Unit
) {
    if (tagList.isEmpty()) {
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
            onDeleteTagClick = { onDeleteTagClick(this) }
        )
}

@Composable
private fun TagRowWithClose(
    tagList: List<TagEntity>?,
    onTagClick: TagEntity.() -> Unit,
    onDeleteTagClick: TagEntity.() -> Unit
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