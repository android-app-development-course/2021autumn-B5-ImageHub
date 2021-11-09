package com.hyosakura.imagehub.ui

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BasePage() {
    val state = remember { BasePageState() }
    NavigationBar(state)
}

@Preview
@Composable
fun BasePagePreview() {
    BasePage()
}