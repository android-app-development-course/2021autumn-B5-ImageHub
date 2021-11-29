package com.hyosakura.imagehub.ui.screens.search

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun SearchResultsScreen() {

    var searchString by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column {
                val keyboardController = LocalSoftwareKeyboardController.current
                OutlinedTextField(
                    value = searchString,
                    onValueChange = { searchString = it },
                    placeholder = {
                        Text(
                            stringResource(R.string.searchSuggestions),
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
                Divider(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 52.dp)
                )
            }
        },
        content = {
            Text("SearchResultsScreen")
        },
    )
}

@Preview
@Composable
fun DefaultPreview() {
    SearchResultsScreen()
}