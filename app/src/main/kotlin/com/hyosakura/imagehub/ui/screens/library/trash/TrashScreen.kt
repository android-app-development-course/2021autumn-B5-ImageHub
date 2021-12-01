package com.hyosakura.imagehub.ui.screens.library.trash

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.ui.screens.library.folder.FolderItem

@OptIn(ExperimentalFoundationApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen() {
    val imageList = mutableListOf<Int>(1,2,3,4,5)

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text("回收站") },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "回收站"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }) {
        Column(Modifier.fillMaxWidth()) {
            Text(text = "回收站回收规则")
            Text(text = "回收站回收规则")
            Text(text = "回收站回收规则")
            Text(text = "回收站回收规则")

            LazyVerticalGrid(
                cells = GridCells.Adaptive(minSize = 120.dp),
            ) {
                items(imageList.size) { id ->
                    ImageItem(imageList, id)
                }
            }
        }
    }


}

@Composable
fun ImageItem(imageList: MutableList<Int>, id: Int) {
    TextButton(onClick = { /*TODO*/ }) {
        Image(
            painter = painterResource(R.drawable.ic_outline_image_black_24),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
        )
    }
}
