package com.hyosakura.imagehub.ui.screens.search


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onSearchBarClick: () -> Unit) {

    // TODO 获取星标标签（按最近使用排序）和最近使用标签

    Scaffold(
        topBar = {
            Row(
                Modifier.fillMaxWidth().fillMaxHeight(0.08f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = onSearchBarClick,
                    modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight()
                ) {
                    Row(
                        Modifier.fillMaxWidth().alpha(0.7f),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.searchSuggestions), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        content = {

        }
    )
}
