package com.hyosakura.imagehub.ui.screens.library

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
import com.hyosakura.imagehub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceImageScreen() {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text("导入图片") },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
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
                onClick = { /*TODO*/ },
                elevation = ButtonDefaults.elevatedButtonElevation(hoveredElevation = 6.dp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text(text = "立刻导入", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.size(150.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_label_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                        )
                        Text(text = "添加标签", style = MaterialTheme.typography.titleMedium)
                    }
                }
                OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.size(150.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_folder_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                        )
                        Text(text = "添加文件夹", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

