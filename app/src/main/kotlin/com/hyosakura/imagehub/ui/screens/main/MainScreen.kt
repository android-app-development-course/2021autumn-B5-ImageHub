package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.entity.toDateTime
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.viewmodel.ImageListViewModel
import com.hyosakura.imagehub.viewmodel.ImageListViewModelFactory
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

@Composable
fun MainScreen(
    repository: DataRepository,
    navController: NavHostController
) {
    val viewModel: ImageListViewModel = ImageListViewModelFactory(repository).create(ImageListViewModel::class.java)
    val format = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    Column {
        viewModel.imageList.observeAsState().value?.let { entityList ->
            val map = entityList.stream().collect(Collectors.groupingBy {
                it.addTime!!.toDateTime().toLocalDate()
            })
            val iterator = map.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                val date = entry.key
                val list = entry.value
                ImageListWithDate(
                    date.format(format),
                    list.map { it },
                    navController
                )
            }
        }
    }
}