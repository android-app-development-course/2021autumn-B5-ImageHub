package com.hyosakura.imagehub.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun NavigationBar(state: BasePageState) {

    NavigationBar() {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text("主页") },
            selected = state.selectedItem == 0,
            onClick = { state.selectedItem = 0 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = null) },
            label = { Text("搜索") },
            selected = state.selectedItem == 1,
            onClick = { state.selectedItem = 1 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
            label = { Text("管理") },
            selected = state.selectedItem == 2,
            onClick = { state.selectedItem = 2 }
        )
    }
}

@Preview
@Composable
fun NavigationBarPreview() {
    NavigationBar(BasePageState())
}