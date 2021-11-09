package com.hyosakura.imagehub.ui.page

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.hyosakura.imagehub.R

class Item(var text: String, var painter1: Painter, var painter2: Painter = painter1)

@Composable
fun NavigationBar(state: BasePageState) {

    val items = listOf(
        Item(
            "图片",
            painterResource(id = R.drawable.ic_baseline_image_24),
            painterResource(id = R.drawable.ic_outline_image_24)
        ),
        Item(
            "搜索",
            painterResource(id = R.drawable.ic_baseline_search_24),
            painterResource(id = R.drawable.ic_outline_search_24)
        ),
        Item(
            "资源库",
            painterResource(id = R.drawable.ic_baseline_storage_24),
            painterResource(id = R.drawable.ic_outline_storage_24)
        )
    )

    NavigationBar {

        for ((index, item) in items.withIndex()) {
            NavigationBarItem(
                icon = {
                    if (state.currentPage == index)
                        Icon(item.painter1, contentDescription = null)
                    else
                        Icon(item.painter2, contentDescription = null)
                },
                label = { Text(item.text, style = MaterialTheme.typography.labelLarge) },
                selected = state.currentPage == index,
                onClick = { state.currentPage = index }
            )
        }
    }
}

@Preview
@Composable
fun NavigationBarPreview() {
    NavigationBar(BasePageState())
}