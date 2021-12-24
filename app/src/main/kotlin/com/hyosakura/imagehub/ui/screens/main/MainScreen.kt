package com.hyosakura.imagehub.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.viewmodel.ImageManageViewModel
import com.hyosakura.imagehub.viewmodel.ImageManageViewModelFactory

@Composable
fun MainScreen(
    repository: DataRepository,
    navController: NavHostController,
    viewModel: ImageManageViewModel = viewModel(factory = ImageManageViewModelFactory(repository))
) {
    viewModel.imageList.observeAsState().value?.let { entityList ->
        ImageList(entityList, navController)
    }
}