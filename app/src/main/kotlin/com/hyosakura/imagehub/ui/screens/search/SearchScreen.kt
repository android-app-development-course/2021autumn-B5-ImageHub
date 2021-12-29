package com.hyosakura.imagehub.ui.screens.search


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.HistoryEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.ui.composables.MiniTagRowHistory
import com.hyosakura.imagehub.ui.composables.SearchBarButton
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
    Scaffold(
        topBar = {
            SearchBarButton(stringResource(R.string.searchTagAnnotation), onSearchBarClick)
        },
        content = {
            LazyColumn(modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp)) {
                if (!searchHistory.isNullOrEmpty()) {
                    item {
                        Text(
                            stringResource(R.string.searchHistory)
                        )
                        MiniTagRowHistory(
                            tagList = searchHistory,
                            onSuggestTagClick = onSearchHistoryClick
                        )
                    }
                }
                if (!recentTags.isNullOrEmpty()) {
                    item {
                        Text(text = stringResource(R.string.recentlyUsedTags))
                        TagRow(tagList = recentTags, onSuggestTagClick = onSuggestTagClick)
                    }
                }
                if (!starTags.isNullOrEmpty()) {
                    item {
                        Text(text = stringResource(id = R.string.starTags))
                        TagRow(tagList = starTags, onSuggestTagClick = onSuggestTagClick)
                    }
                }
            }
        }
    )
}