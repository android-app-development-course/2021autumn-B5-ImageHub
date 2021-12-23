package com.hyosakura.imagehub.ui.screens.library.label

import androidx.compose.foundation.layout.Column
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