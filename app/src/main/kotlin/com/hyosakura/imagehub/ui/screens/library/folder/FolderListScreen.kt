package com.hyosakura.imagehub.ui.screens.library.folder

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R

@OptIn(
    ExperimentalMaterial3Api::class,
    androidx.compose.foundation.ExperimentalFoundationApi::class
)
@Composable
fun FolderScreen()  {
    val folderList = listOf("folder1", "folder2", "folder3")

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text("文件夹列表") },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "文件夹列表"
                        )
                    }
                }
            )
        }) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 180.dp),
            contentPadding = PaddingValues(10.dp),
        ) {
            items(folderList.size) { id ->
                FolderItem(id, folderList)
            }
        }
    }
}

@Composable
fun FolderItem(id: Int, folderList: List<String>) {
    Surface(modifier = Modifier.size(180.dp)) {
        Column {
            TextButton(onClick = { /*TODO*/ }) {
                Image(
                    painter = painterResource(R.drawable.ic_outline_image_black_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(130.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = folderList[id], style = MaterialTheme.typography.titleMedium)

        }
    }
}

@Preview
@Composable
fun FolderScreenTest() {
    FolderScreen()
}
