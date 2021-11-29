package com.hyosakura.imagehub.ui.screens.library

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.ui.screens.Screen.*

@Composable
fun LibraryScreen(navController: NavHostController) {
    Column {
        // 上半部分
        Column(Modifier.padding(20.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    iconId = R.drawable.ic_outline_label_24, textId = R.string.label,
                    onButtonClick = { navController.navigate(Label.name) })
                Button(
                    iconId = R.drawable.ic_outline_folder_24, textId = R.string.folder,
                    onButtonClick = { navController.navigate(Folder.name) })
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    iconId = R.drawable.ic_outline_tip_24, textId = R.string.tip,
                    onButtonClick = { navController.navigate(Tip.name) })
                Button(
                    iconId = R.drawable.ic_baseline_delete_outline_24, textId = R.string.recycleBin,
                    onButtonClick = { navController.navigate(Trash.name) }
                )
            }
        }
        // 下半部分
        Column {

        }
    }
}

@Composable
private fun Button(iconId: Int, textId: Int, onButtonClick: () -> Unit) {
    FilledTonalButton(onClick = onButtonClick, modifier = Modifier.size(147.dp, 60.dp)) {
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

