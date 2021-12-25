package com.hyosakura.imagehub.ui.screens.library.label

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.main.ImageList
import com.hyosakura.imagehub.viewmodel.TagManageViewModel
import com.hyosakura.imagehub.viewmodel.TagManageViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelImageScreen(
    repository: DataRepository,
    labelId: Int?,
    navController: NavHostController,
    tagManageViewModel: TagManageViewModel = viewModel(factory = TagManageViewModelFactory(repository))
) {
    Scaffold(topBar = {
        SmallTopAppBar(
            // TODO: 获取标签名称放在下面
            title = { Text("标签名称放这里") },
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
        tagManageViewModel.getImageInTag(labelId!!).observeAsState().value?.let {entityList->
            ImageList(entityList, navController)
        }
    }
}