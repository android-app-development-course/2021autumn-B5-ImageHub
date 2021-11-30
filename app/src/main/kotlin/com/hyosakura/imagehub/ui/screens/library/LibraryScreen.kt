package com.hyosakura.imagehub.ui.screens.library

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.ui.screens.Screen.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LibraryScreen(navController: NavHostController) {
    Column(Modifier.fillMaxSize()) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (label, folder, tip, trash) = createRefs()
            Button(
                iconId = R.drawable.ic_outline_label_24, textId = R.string.label,
                onButtonClick = { navController.navigate(Label.name) },
                modifier = Modifier.constrainAs(label) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(folder.start, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                })
            Button(
                iconId = R.drawable.ic_outline_folder_24, textId = R.string.folder,
                onButtonClick = { navController.navigate(Folder.name) },
                modifier = Modifier.constrainAs(folder) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(label.end, margin = 8.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                })
            Button(
                iconId = R.drawable.ic_outline_tip_24, textId = R.string.tip,
                onButtonClick = { navController.navigate(Tip.name) },
                modifier = Modifier.constrainAs(tip) {
                    top.linkTo(label.bottom, margin = 16.dp)
                    end.linkTo(trash.start, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                })
            Button(
                iconId = R.drawable.ic_baseline_delete_outline_24, textId = R.string.recycleBin,
                onButtonClick = { navController.navigate(Trash.name) },
                modifier = Modifier.constrainAs(trash) {
                    top.linkTo(folder.bottom, margin = 16.dp)
                    start.linkTo(tip.end, margin = 8.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
            )
        }

        // 下半部分
        Column {

        }
    }
}

@Composable
private fun Button(iconId: Int, textId: Int, onButtonClick: () -> Unit, modifier: Modifier) {
    FilledTonalButton(onClick = onButtonClick, modifier = modifier.height(70.dp).width(150.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(stringResource(textId), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview
@Composable
fun LibraryScreenPreview() {
    val navController = rememberNavController()
    LibraryScreen(navController)
}

