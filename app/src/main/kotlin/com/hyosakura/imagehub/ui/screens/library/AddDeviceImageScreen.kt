package com.hyosakura.imagehub.ui.screens.library

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.ui.screens.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceImageScreen(imageId: Int?, navController: NavHostController) {

    // TODO 根据图片 Id 获取图片
    val image: ImageEntity = ImageEntity()

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
                    /*TODO 保存图片对象到数据库*/
                    navController.popBackStack()
                    navController.navigate("${Screen.Detail.name}/${image.imageId}")
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

