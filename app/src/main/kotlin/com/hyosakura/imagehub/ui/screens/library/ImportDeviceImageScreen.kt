package com.hyosakura.imagehub.ui.screens.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
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
    //TODO：分别获取常用标签和星标标签两个列表
    val image = viewModel.getImageById(imageId!!)

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text("添加图片") },
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
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            if (image != null) {
                Image(
                    bitmap = image.bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                    .fillMaxWidth(0.9f)
                    .height(80.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_download_24),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "立刻添加", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

