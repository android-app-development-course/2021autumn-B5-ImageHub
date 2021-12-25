package com.hyosakura.imagehub.ui.screens.search


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.viewmodel.TagManageViewModel
import com.hyosakura.imagehub.viewmodel.TagManageViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    repository: DataRepository,
    onSearchBarClick: () -> Unit,
    viewModel: TagManageViewModel = viewModel(factory = TagManageViewModelFactory(repository))
) {

    val starTags by viewModel.starTags.observeAsState()
    val num = 20
    val recentTags by viewModel.getRecentTag(num).observeAsState()

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

        }
    )
}
