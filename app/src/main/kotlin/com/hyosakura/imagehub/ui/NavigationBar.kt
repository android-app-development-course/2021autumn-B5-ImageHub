package com.hyosakura.imagehub.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.ui.screens.Screen

class Item(var text: String, var painter1: Painter, var painter2: Painter = painter1)

@Composable
fun BaseBottomBar(allScreens: List<Screen>, onSelected: (Screen) -> Unit, currentScreen: Screen) {

    NavigationBar {

        for (screen in allScreens) {
            NavigationBarItem(
                icon = {
                    if (currentScreen == screen)
                        Icon(painterResource(screen.spIconID), contentDescription = null)
                    else
                        Icon(painterResource(screen.iconId), contentDescription = null)
                },
                label = { Text(stringResource(screen.stringId), style = MaterialTheme.typography.labelLarge) },
                selected = currentScreen == screen,
                onClick = { onSelected(screen) }
            )
        }
    }
}

@Preview
@Composable
fun NavigationBarPreview() {

}