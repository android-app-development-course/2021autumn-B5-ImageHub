package com.hyosakura.imagehub.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

@Composable
fun SearchBarButton(searchSuggestion: String, onSearchBarClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp),
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
                    searchSuggestion,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBarTextField(searchAction: (String) -> Unit) {
    var searchString by remember { mutableStateOf("") }
    Column {
        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            value = searchString,
            onValueChange = {
                searchString = it
                if (searchString.isNotBlank()) {
                    searchAction(searchString)
                }
            },
            placeholder = {
                Text(
                    stringResource(R.string.searchTagAnnotation),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.alpha(0.7f)
                )
            },
            textStyle = MaterialTheme.typography.titleMedium,
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_outline_search_24),
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.inversePrimary,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions { keyboardController?.hide() },
            modifier = Modifier.fillMaxWidth()
        )
        TabRowDefaults.Divider(
            color = MaterialTheme.colorScheme.inversePrimary,
            thickness = 1.dp,
            modifier = Modifier.padding(start = 52.dp)
        )
    }
}
