package com.hyosakura.imagehub.ui.screens.library.tag

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.main.ImageList
import com.hyosakura.imagehub.viewmodel.TagManageViewModel
import com.hyosakura.imagehub.viewmodel.TagManageViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagImageScreen(
    repository: DataRepository,
    tagId: Int?,
    navController: NavHostController,
    tagManageViewModel: TagManageViewModel = viewModel(
        factory = TagManageViewModelFactory(
            repository
        )
    )
) {
    Scaffold(topBar = {
        SmallTopAppBar(
            title = {
                tagManageViewModel.visitTag(tagId!!).observeAsState().value?.let {
                    Text(it.name!!)
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            }
        )
    }) {
        tagManageViewModel.getImageInTag(tagId!!).observeAsState().value?.let { entityList ->
            ImageList(entityList, navController)
        }
    }
}