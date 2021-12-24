package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.viewmodel.ImageListViewModel
import com.hyosakura.imagehub.viewmodel.ImageListViewModelFactory

@Composable
fun MainScreen(
    repository: DataRepository,
    navController: NavHostController
) {
    val viewModel: ImageListViewModel = ImageListViewModelFactory(repository).create(ImageListViewModel::class.java)
    viewModel.imageList.observeAsState().value?.let { entityList ->
       ImageList(entityList, navController)
    }
}