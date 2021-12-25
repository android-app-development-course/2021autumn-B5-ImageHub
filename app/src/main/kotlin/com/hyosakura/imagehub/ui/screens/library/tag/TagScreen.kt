package com.hyosakura.imagehub.ui.screens.library.tag

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.toDateTime
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.Screen
import com.hyosakura.imagehub.viewmodel.TagManageViewModel
import com.hyosakura.imagehub.viewmodel.TagManageViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.stream.Collectors

private val coroutine = CoroutineScope(Dispatchers.IO)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagScreen(
    repository: DataRepository,
    navController: NavHostController,
    viewModel: TagManageViewModel = viewModel(factory = TagManageViewModelFactory(repository))
) {
    var isEditMode by remember { mutableStateOf(false) }
    var isAddMode by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text("标签列表") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "标签列表"
                        )
                    }
                }
            )
        }
    ) {
        Column(Modifier.fillMaxSize()) {
            val entityList by viewModel.allTags.observeAsState()
            if (entityList != null) {
                val map = entityList!!.stream().collect(Collectors.groupingBy {
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
                            var isStarChange by remember { mutableStateOf(tag.star) }
                            TagItem(
                                tag,
                                isStarChange,
                                onEditClick = {
                                    isEditMode = true
                                    currentTag = tag
                                },
                                onLabelClick = {
                                    navController.navigate("${Screen.TagImage.name}/${tag.tagId}")
                                    currentTag = tag
                                },
                                onStarClick = {
                                    tag.star = if (tag.star == 0) 1 else 0
                                    viewModel.updateTag(tag)
                                    isStarChange = tag.star
                                },
                                onDeleteClick = { labelToDelete ->
                                    viewModel.deleteTag(labelToDelete)
                                }
                            )
                        }
                    }

                    if (isEditMode) {
                        var editText by remember { mutableStateOf(currentTag!!.name!!) }
                        AlertDialog(
                            onDismissRequest = { isEditMode = false },
                            title = {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Row(Modifier.fillMaxWidth()) {
                                    Text(text = "编辑标签")
                                    TextButton(
                                        onClick = {
                                            viewModel.deleteTag(currentTag!!)
                                            isEditMode = false
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = null
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
                                        viewModel.updateTag(currentTag!!)
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
                    onDismissRequest = { isAddMode = false },
                    title = { Text(text = "添加标签") },
                    text = {
                        OutlinedTextField(
                            value = editText,
                            onValueChange = { editText = it },
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
                        TextButton(onClick = {
                            isAddMode = false
                            coroutine.launch {
                                val list = viewModel.getTagByNameWithOutFlow(editText, false)
                                if (list.isEmpty()) {
                                    viewModel.insertTag(
                                        TagEntity(
                                            name = editText,
                                            addTime = System.currentTimeMillis()
                                        )
                                    )
                                } else {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "标签已存在",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    Log.i("tag", "已经有标签")
                                }
                            }
                        }) { Text("添加标签") }
                    },
                    dismissButton = {
                        TextButton(onClick = { isAddMode = false }) { Text("取消") }
                    })
            }
        }
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val fab = createRef()
            ExtendedFloatingActionButton(
                onClick = {
                    isAddMode = !isAddMode
                },
                icon = { Icon(Icons.Filled.Add, "添加标签") },
                text = { Text(text = "添加标签") },
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
    onLabelClick: () -> Unit,
    onDeleteClick: (TagEntity) -> Unit
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
                Text(tagEntity.name!!, style = MaterialTheme.typography.titleMedium)
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth())

        TextButton(
            onClick = onEditClick, modifier = Modifier
                .requiredSize(60.dp)
                .offset((-90).dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = null
            )
        }
        TextButton(
            onClick = { onDeleteClick(tagEntity) }, modifier = Modifier
                .requiredSize(60.dp)
                .offset((-30).dp, 0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
