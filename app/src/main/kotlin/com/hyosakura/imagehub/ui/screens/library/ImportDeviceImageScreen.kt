package com.hyosakura.imagehub.ui.screens.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.Screen
import com.hyosakura.imagehub.viewmodel.DeviceImageViewModel
import com.hyosakura.imagehub.viewmodel.DeviceImageViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val coroutine = CoroutineScope(Dispatchers.Main)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportDeviceImageScreen(
    repository: DataRepository,
    imageId: Int?,
    navController: NavHostController,
    viewModel: DeviceImageViewModel = viewModel(factory = DeviceImageViewModelFactory(repository))
) {
    val image = viewModel.getImageById(imageId!!)

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text("添加图片") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "导入图片"
                        )
                    }
                }
            )
        }) {
        Column(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.ic_outline_image_black_24),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            FilledTonalButton(
                onClick = {
                    coroutine.launch {
                        val id = viewModel.importImage(image!!)
                        navController.popBackStack()
                        navController.navigate("${Screen.Detail.name}/$id")
                    }
                },
                elevation = ButtonDefaults.elevatedButtonElevation(hoveredElevation = 6.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text(text = "立刻添加", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

