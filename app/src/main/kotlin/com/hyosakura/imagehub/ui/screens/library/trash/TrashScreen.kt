package com.hyosakura.imagehub.ui.screens.library.trash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.main.ImageList
import com.hyosakura.imagehub.viewmodel.RecycleBinViewModel
import com.hyosakura.imagehub.viewmodel.RecycleBinViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(
    repository: DataRepository,
    navController: NavHostController,
    viewModel: RecycleBinViewModel = viewModel(factory = RecycleBinViewModelFactory(repository))
) {
    Scaffold(topBar = {
        SmallTopAppBar(
            title = { Text("回收站") },
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
        Column {
            Text(
                text = stringResource(R.string.trashDeleteRule),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(20.dp)
            )
            viewModel.allDeletedImages.observeAsState().value?.let { entityList ->
                ImageList(entityList, navController)
            }
        }
    }
}