package com.hyosakura.imagehub.ui.screens.library.label

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.hyosakura.imagehub.entity.toDateTime
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.viewmodel.TagManageViewModel
import com.hyosakura.imagehub.viewmodel.TagManageViewModelFactory
import java.util.stream.Collectors

@Composable
fun LabelScreen(
    repository: DataRepository,
    viewModel: TagManageViewModel = TagManageViewModelFactory(repository).create(
        TagManageViewModel::class.java
    )
) {
    TopAppBar(
        title = { Text("这是标题") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = {
                // viewModel.insertTag("tag名")
            }) {
                Icon(Icons.Filled.Share, "添加")
            }
            IconButton(onClick = {
                // viewModel.updateTag()
            }) {
                Icon(Icons.Filled.Settings, "修改")
            }
            IconButton(onClick = {
                // viewModel.deleteTag()
            }) {
                Icon(Icons.Filled.Settings, "删除")
            }
        }
    )
    Column {
        viewModel.allTags.observeAsState().value?.let { entityList ->
            val map = entityList.stream().collect(Collectors.groupingBy {
                it.addTime!!.toDateTime().toLocalDate()
            })
            val iterator = map.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                val date = entry.key
                val list = entry.value
                // 展示tag
            }
        }
    }
}