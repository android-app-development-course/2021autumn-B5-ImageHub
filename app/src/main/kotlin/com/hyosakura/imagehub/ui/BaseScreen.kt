package com.hyosakura.imagehub.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.ui.screens.LibraryScreen
import com.hyosakura.imagehub.ui.screens.Screen
import com.hyosakura.imagehub.ui.screens.Screen.*
import com.hyosakura.imagehub.ui.screens.main.MainScreen
import com.hyosakura.imagehub.ui.screens.search.SearchResultsScreen
import com.hyosakura.imagehub.ui.screens.search.SearchScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen() {

    val allScreens = values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = Screen.fromRoute(backstackEntry.value?.destination?.route)

    Scaffold(
        topBar = {
            TopBar(currentScreen)
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Main.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Main.name) {
                    MainScreen()
                }
                composable(Search.name) {
                    SearchScreen(onSearchBarClick = { navController.navigate(SearchResults.name) })
                }
                composable(Library.name) {
                    LibraryScreen()
                }
                composable(SearchResults.name) {
                    SearchResultsScreen()
                }
            }
        },
        bottomBar = {
            BottonBar(navController, currentScreen)
        }
    )
}

@Composable
private fun BottonBar(
    navController: NavHostController,
    currentScreen: Screen
) {
    when (currentScreen) {
        Main -> BaseBottomBar(
            allScreens = listOf(Main, Search, Library),
            onSelected = { screen ->
                navController.navigate(screen.name)
            },
            currentScreen = currentScreen,
        )
        Search -> BaseBottomBar(
            allScreens = listOf(Main, Search, Library),
            onSelected = { screen ->
                navController.navigate(screen.name)
            },
            currentScreen = currentScreen,
        )
        Library -> BaseBottomBar(
            allScreens = listOf(Main, Search, Library),
            onSelected = { screen ->
                navController.navigate(screen.name)
            },
            currentScreen = currentScreen,
        )
        SearchResults -> {
        }
    }
}

@Composable
private fun TopBar(currentScreen: Screen) {
    // TODO: 用动画实现消失效果
    when (currentScreen) {
        Main -> BaseTopBar()
        Search -> BaseTopBar()
        Library -> BaseTopBar()
        SearchResults -> {
        }
    }
}

@Composable
private fun BaseTopBar() {
    CenterAlignedTopAppBar(title = {
        Text(
            stringResource(R.string.imageHub),
            style = MaterialTheme.typography.headlineLarge
        )
    })
}

@Preview
@Composable
fun BasePagePreview() {
    BaseScreen()
}