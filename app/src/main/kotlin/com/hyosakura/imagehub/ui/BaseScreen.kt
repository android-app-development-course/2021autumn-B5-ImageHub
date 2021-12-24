package com.hyosakura.imagehub.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.Screen
import com.hyosakura.imagehub.ui.screens.Screen.*
import com.hyosakura.imagehub.ui.screens.library.LibraryScreen
import com.hyosakura.imagehub.ui.screens.library.folder.FolderScreen
import com.hyosakura.imagehub.ui.screens.library.label.LabelScreen
import com.hyosakura.imagehub.ui.screens.library.tip.TipScreen
import com.hyosakura.imagehub.ui.screens.library.trash.TrashScreen
import com.hyosakura.imagehub.ui.screens.main.DetailScreen
import com.hyosakura.imagehub.ui.screens.main.MainScreen
import com.hyosakura.imagehub.ui.screens.search.SearchResultsScreen
import com.hyosakura.imagehub.ui.screens.search.SearchScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    repository: DataRepository
) {
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
                    MainScreen(repository, navController)
                }
                composable(Search.name) {
                    SearchScreen(onSearchBarClick = { navController.navigate(SearchResults.name) })
                }
                composable(Library.name) {
                    LibraryScreen(navController)
                }
                composable(SearchResults.name) {
                    SearchResultsScreen(repository, navController)
                }
                composable(Label.name) {
                    LabelScreen(repository)
                }
                composable(Folder.name) {
                    FolderScreen(repository)
                }
                composable(Tip.name) {
                    TipScreen()
                }
                composable(Trash.name) {
                    TrashScreen(repository, navController)
                }
                composable(
                    "${ Detail.name }/{imageId}",
                    arguments = listOf(navArgument("imageId") { type = NavType.IntType })){
                    DetailScreen(it.arguments?.getInt("imageId"), repository, navController)
                }
            }
        },
        bottomBar = {
            BottomBar(navController, currentScreen)
        }
    )
}

@Composable
private fun TopBar(currentScreen: Screen) {
    AnimatedVisibility(currentScreen == Main || currentScreen == Search || currentScreen == Library) {
        BaseTopBar()
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

@Composable
private fun BottomBar(
    navController: NavHostController,
    currentScreen: Screen
) {
    AnimatedVisibility(
        currentScreen == Main || currentScreen == Search || currentScreen == Library,
    ) {
        BaseBottomBar(
            allScreens = listOf(Main, Search, Library),
            onSelected = { screen ->
                navController.navigate(screen.name)
            },
            currentScreen = currentScreen,
        )
    }
}

@Preview
@Composable
fun BasePagePreview() {
    // BaseScreen()
}