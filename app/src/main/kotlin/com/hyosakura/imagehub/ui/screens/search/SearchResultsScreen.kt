package com.hyosakura.imagehub.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.toDateTime
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.main.ImageListWithDate
import com.hyosakura.imagehub.viewmodel.ImageManageViewModel
import com.hyosakura.imagehub.viewmodel.ImageManageViewModelFactory
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun SearchResultsScreen(
    repository: DataRepository,
    navController: NavHostController,
    viewModel: ImageManageViewModel = viewModel(factory = ImageManageViewModelFactory(repository))
) {
    var searchString by remember { mutableStateOf("") }
    val format = DateTimeFormatter.ofPattern("yyyy/MM/dd")

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
            Column {
                viewModel.searchImage(searchString)
                viewModel.imageList.observeAsState().value?.let { entityList ->
                    val map = entityList.stream().collect(Collectors.groupingBy {
                        it.addTime!!.toDateTime().toLocalDate()
                    })
                    val iterator = map.iterator()
                    while (iterator.hasNext()) {
                        val entry = iterator.next()
                        val date = entry.key
                        val list = entry.value
                        ImageListWithDate(
                            date.format(format), list.map { it }, navController
                        )
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun DefaultPreview() {
    // SearchResultsScreen()
}