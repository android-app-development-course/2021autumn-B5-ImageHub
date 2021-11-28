package com.hyosakura.imagehub.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen() {

    var searchString by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(Purple80),
                        verticalAlignment = Alignment.CenterVertically)
                    {
                        TextField(
                            value = searchString,
                            onValueChange = { searchString = it },
                            label = { Text("搜索“标签”“注释”…") },
                            singleLine = true
                        )
                    }
                }
            )
        },
        content = {
            Text("Page1")
        }
    )
}

@Preview
@Composable
fun DefaultPreview() {
    SearchResultsScreen()
}