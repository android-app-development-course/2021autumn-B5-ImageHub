package com.hyosakura.imagehub.ui.screens.search


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onSearchBarClick: () -> Unit) {

    val lableList = mutableListOf<Int>(1, 2, 3, 4, 5)

    Scaffold(
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.08f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = onSearchBarClick,
                    modifier = Modifier
                        .width(320.dp)
                        .fillMaxHeight()
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .alpha(0.7f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.searchSuggestions),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        },
        content = {

            Column(Modifier.padding(top = 40.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "常用标签",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }

                LazyRow() {
                    items(lableList.size) { id ->
                        LabelItem(lableList, id)
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "常用文件夹",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }

                LazyRow() {
                    items(lableList.size) { id ->
                        LabelItem(lableList, id)
                    }
                }
            }
        }
    )
}

@Composable
fun LabelItem(lableList: MutableList<Int>, id: Int) {
    Column {
        TextButton(onClick = { /*TODO*/ }) {
            Image(
                painter = painterResource(R.drawable.ic_outline_image_black_24),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
            )
        }
        Text(
            text = "TestLabel",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 12.dp, top = 8.dp)
        )
    }
}
