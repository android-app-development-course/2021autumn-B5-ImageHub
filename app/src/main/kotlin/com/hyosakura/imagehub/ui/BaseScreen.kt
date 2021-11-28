package com.hyosakura.imagehub.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hyosakura.imagehub.ui.screens.LibraryScreen
import com.hyosakura.imagehub.ui.screens.Screen
import com.hyosakura.imagehub.ui.screens.main.MainScreen
import com.hyosakura.imagehub.ui.screens.search.SearchResultsScreen
import com.hyosakura.imagehub.ui.screens.search.SearchScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen() {

    val allScreens = Screen.values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = Screen.fromRoute(backstackEntry.value?.destination?.route)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    "Image Hub",
                    style = MaterialTheme.typography.headlineLarge
                )
            })
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Main.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Main.name) {
                    MainScreen()
                }
                composable(Screen.Search.name) {
                    SearchScreen()
                }
                composable(Screen.Library.name) {
                    LibraryScreen()
                }
                composable(Screen.SearchResults.name) {
                    SearchResultsScreen()
                }
            }
        },
        bottomBar = {
            BaseBottomBar(
                allScreens = listOf(Screen.Main, Screen.Search, Screen.Library),
                onSelected = { screen ->
                    navController.navigate(screen.name)
                },
                currentScreen = currentScreen,
            )
        }
    )
}

@Preview
@Composable
fun BasePagePreview() {
    BaseScreen()
}