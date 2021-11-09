package com.hyosakura.imagehub.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
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
                    if (state.selectedItem == index)
                        Icon(item.painter1, contentDescription = null)
                    else
                        Icon(item.painter2, contentDescription = null)
                },
                label = { Text(item.text, fontSize = 14.sp) },
                selected = state.selectedItem == index,
                onClick = { state.selectedItem = index }
            )
        }
    }
}

@Preview
@Composable
fun NavigationBarPreview() {
    NavigationBar(BasePageState())
}