package com.hyosakura.imagehub.ui.screens.library.label

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.main.ImageList
import com.hyosakura.imagehub.viewmodel.TagManageViewModel
import com.hyosakura.imagehub.viewmodel.TagManageViewModelFactory


@Composable
fun LabelImageScreen(
    repository: DataRepository,
    labelId: Int?,
    navController: NavHostController,
    tagManageViewModel: TagManageViewModel = viewModel(factory = TagManageViewModelFactory(repository))
) {
    Text(text = labelId.toString())
    tagManageViewModel.getImageInTag(labelId!!).observeAsState().value?.let {entityList->
       ImageList(entityList, navController)
    }
}