package com.hyosakura.imagehub.ui.screens.search


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    Scaffold(
        topBar = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = { /* Do something! */ },
                    modifier = Modifier.width(320.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().alpha(0.7f),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("搜索 “标签” “注释”", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        content = {
            Text("Page1")
        }
    )
}

@Preview
@Composable
fun PreviewSearchPage() {
    SearchScreen()
}
