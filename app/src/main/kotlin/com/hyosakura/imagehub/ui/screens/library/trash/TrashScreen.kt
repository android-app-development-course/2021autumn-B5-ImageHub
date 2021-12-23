package com.hyosakura.imagehub.ui.screens.library.trash

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.entity.toDateTime
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.main.ImageListWithDate
import com.hyosakura.imagehub.viewmodel.RecycleBinViewModel
import com.hyosakura.imagehub.viewmodel.RecycleBinViewModelFactory
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

@Composable
fun TrashScreen(
    repository: DataRepository,
    navController: NavHostController,
) {
    val viewModel: RecycleBinViewModel = RecycleBinViewModelFactory(repository).create(
        RecycleBinViewModel::class.java
    )
    val format = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    Column {
        viewModel.allDeletedImages.observeAsState().value?.let { entityList ->
            val map = entityList.stream().collect(Collectors.groupingBy {
                it.addTime!!.toDateTime().toLocalDate()
            })
            val iterator = map.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                val date = entry.key
                val list = entry.value
                ImageListWithDate(
                    date.format(format), list.map { it }, navController
                )
            }
        }
    }
}