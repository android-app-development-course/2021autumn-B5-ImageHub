package com.hyosakura.imagehub.ui.screens.search


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.hyosakura.imagehub.entity.HistoryEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.ui.composables.MiniTagRowHistory
import com.hyosakura.imagehub.ui.composables.TagRow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    starTags: List<TagEntity>?,
    recentTags: List<TagEntity>?,
    searchHistory: List<HistoryEntity>?,
    onSearchBarClick: () -> Unit,
    onSuggestTagClick: (TagEntity) -> Unit,
    onSearchHistoryClick: (String) -> Unit
) {
    Log.i("recent", recentTags.toString())
    Log.i("star", starTags.toString())
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
                        .fillMaxWidth(0.9f)
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
            LazyColumn(modifier = Modifier.padding(16.dp, 16.dp, 0.dp,0.dp)) {
                if (!searchHistory.isNullOrEmpty()) {
                    item {
                        Text(
                            stringResource(R.string.searchHistory)
                        )
                        MiniTagRowHistory(tagList = searchHistory, onSuggestTagClick = onSearchHistoryClick)
                    }
                }
                if (!recentTags.isNullOrEmpty()){
                    item {
                        Text(text = stringResource(R.string.recentlyUsedTags))
                        TagRow(tagList = recentTags, onSuggestTagClick =  onSuggestTagClick )
                    }
                }
                if (!starTags.isNullOrEmpty()){
                    item {
                        Text(text = stringResource(id = R.string.starTags))
                        TagRow(tagList = starTags, onSuggestTagClick = onSuggestTagClick)
                    }
                }
            }
        }
    )
}
