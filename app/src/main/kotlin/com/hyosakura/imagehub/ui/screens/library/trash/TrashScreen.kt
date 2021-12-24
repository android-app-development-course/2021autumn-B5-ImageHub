package com.hyosakura.imagehub.ui.screens.library.trash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.main.ImageList
import com.hyosakura.imagehub.viewmodel.RecycleBinViewModel
import com.hyosakura.imagehub.viewmodel.RecycleBinViewModelFactory

@Composable
fun TrashScreen(
    repository: DataRepository,
    navController: NavHostController,
    viewModel: RecycleBinViewModel = viewModel(factory = RecycleBinViewModelFactory(repository))
) {
    viewModel.allDeletedImages.observeAsState().value?.let { entityList ->
       ImageList(entityList, navController)
    }
}