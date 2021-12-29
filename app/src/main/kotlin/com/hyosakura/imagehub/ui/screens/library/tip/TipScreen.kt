package com.hyosakura.imagehub.ui.screens.library.tip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.ui.composables.ImageListWithDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipScreen(
    onBack: () -> Unit,
) {
    Scaffold(topBar = {
        SmallTopAppBar(
            title = { Text(stringResource(id = R.string.tip)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            }
        )
    }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)) {
            Text(
                text = stringResource(R.string.enjoyImageHub),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

}