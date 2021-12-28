package com.hyosakura.imagehub.ui.screens.library.tag

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.toDateTime
import java.util.stream.Collectors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagScreen(
    onBack: () -> Unit,
    allTags: List<TagEntity>?,
    insertAction: (String) -> Unit,
    updateAction: TagEntity.() -> Unit,
    deleteAction: TagEntity.() -> Unit,
    onTagClick: TagEntity.() -> Unit,
    onTagConflict: () -> Unit
) {
    var isEditMode by remember { mutableStateOf(false) }
    var isAddMode by remember { mutableStateOf(false) }
    var flush by remember { mutableStateOf(true) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text("标签列表") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "标签列表"
                        )
                    }
                }
            )
        }
    ) {
        if (flush) {
            Text(text = "compose's bug?", modifier = Modifier.width(0.dp).height(0.dp))
        }
        Column(Modifier.fillMaxSize()) {
            if (allTags != null) {
                val map = allTags.stream().collect(Collectors.groupingBy {
                    it.addTime!!.toDateTime().toLocalDate()
                })
                val iterator = map.iterator()
                while (iterator.hasNext()) {
                    val entry = iterator.next()
                    val date = entry.key
                    val list = entry.value

                    // 展示tag
                    val lazyListState = rememberLazyListState()

                    var currentTag by remember { mutableStateOf<TagEntity?>(null) }
                    LazyColumn(state = lazyListState, modifier = Modifier) {
                        items(list) { tag ->
                            println(tag)
                            TagItem(
                                tag,
                                tag.star,
                                onStarClick = {
                                    tag.star = if (tag.star == 0) 1 else 0
                                    updateAction(tag)
                                    flush = !flush
                                },
                                onEditClick = {
                                    isEditMode = true
                                    currentTag = tag
                                }
                            ) {
                                onTagClick(tag)
                                currentTag = tag
                            }
                        }
                    }

                    if (isEditMode) {
                        var editText by remember { mutableStateOf(currentTag!!.name!!) }
                        AlertDialog(
                            onDismissRequest = { isEditMode = false },
                            title = {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "编辑标签")
                                    TextButton(
                                        onClick = {
                                            deleteAction(currentTag!!)
                                            isEditMode = false
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp),
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            },
                            text = {
                                OutlinedTextField(
                                    value = editText, onValueChange = { editText = it },
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
                                        isEditMode = false
                                        currentTag!!.name = editText
                                        updateAction(currentTag!!)
                                    }
                                ) { Text("更改标签") }
                            },
                            dismissButton = {
                                TextButton(onClick = { isEditMode = false }) { Text("取消") }
                            }
                        )
                    }
                }
            }


            if (isAddMode) {
                var editText by remember { mutableStateOf("") }
                AlertDialog(
                    onDismissRequest = {
                        isAddMode = false
                    },
                    title = {
                        Text(text = "添加标签")
                    },
                    text = {
                        OutlinedTextField(
                            value = editText,
                            onValueChange = {
                                editText = it
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
                                isAddMode = false
                                allTags?.let {
                                    if (
                                        it.any { t ->
                                            t.name == editText
                                        }
                                    ) {
                                        onTagConflict()
                                    } else {
                                        insertAction(editText)

                                    }
                                }
                            }
                        ) {
                            Text("添加标签")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                isAddMode = false
                            }
                        ) {
                            Text("取消")
                        }
                    }
                )
            }
        }
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val fab = createRef()
            ExtendedFloatingActionButton(
                onClick = {
                    isAddMode = !isAddMode
                },
                icon = { Icon(Icons.Filled.Add, "添加标签") },
                text = { Text(text = stringResource(id = R.string.addTag)) },
                modifier = Modifier.constrainAs(fab) {
                    bottom.linkTo(parent.bottom, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    start.linkTo(parent.start, 16.dp)
                }
            )
        }
    }
}

@Composable
private fun TagItem(
    tagEntity: TagEntity,
    isStar: Int,
    onStarClick: () -> Unit,
    onEditClick: () -> Unit,
    onLabelClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextButton(
            onClick = onStarClick,
            modifier = Modifier.size(60.dp)
        ) {
            if (isStar == 1) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_label_24),
                    contentDescription = null
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_label_24),
                    contentDescription = null
                )
            }
        }

        TextButton(
            onClick = onLabelClick,
            modifier = Modifier
                .height(60.dp)
        ) {
            Row {
                Text(
                    tagEntity.name!!,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth())

        TextButton(
            onClick = onEditClick, modifier = Modifier
                .requiredSize(60.dp)
                .offset((-30).dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = null
            )
        }
    }
}
