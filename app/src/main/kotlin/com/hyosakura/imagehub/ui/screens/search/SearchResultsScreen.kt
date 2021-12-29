package com.hyosakura.imagehub.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.HistoryEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.ui.composables.ImageListWithDate
import com.hyosakura.imagehub.ui.composables.SearchBarTextField

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun SearchResultsScreen(
    searchAction: (String) -> Unit,
    searchResult: List<ImageEntity>?,
    onImageClick: ImageEntity.() -> Unit,
    addSearchHistory: (String) -> Unit
) {
    Scaffold(
        topBar = {
            SearchBarTextField(searchAction)
        },
        content = {
            Column {
                if (!searchResult.isNullOrEmpty()) {
                    ImageListWithDate(
                        images = searchResult,
                        onImageClick = onImageClick
                    )
                } else {
                    Row(Modifier.fillMaxWidth().padding(30.dp), horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = stringResource(R.string.noResult),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                }
            }
        },
    )
}

