package com.hyosakura.imagehub.ui.page

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.hyosakura.imagehub.ui.page.search.SearchPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasePage() {

    val state = remember { BasePageState() }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Image Hub", style = MaterialTheme.typography.headlineLarge) }) },
        content = {
            when (state.currentPage) {
                0 -> PhotosPage(state)
                1 -> SearchPage(state)
                2 -> LibraryPage(state)
            }
        },
        bottomBar = { NavigationBar(state) }
    )
}

@Preview
@Composable
fun BasePagePreview() {
    BasePage()
}