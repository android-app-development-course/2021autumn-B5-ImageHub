package com.hyosakura.imagehub.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.entity.DeviceImageEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.Screen
import com.hyosakura.imagehub.ui.screens.Screen.*
import com.hyosakura.imagehub.ui.screens.library.ImportDeviceImageScreen
import com.hyosakura.imagehub.ui.screens.library.LibraryScreen
import com.hyosakura.imagehub.ui.screens.library.folder.FolderScreen
import com.hyosakura.imagehub.ui.screens.library.tag.TagImageScreen
import com.hyosakura.imagehub.ui.screens.library.tag.TagScreen
import com.hyosakura.imagehub.ui.screens.library.tip.TipScreen
import com.hyosakura.imagehub.ui.screens.library.trash.TrashScreen
import com.hyosakura.imagehub.ui.screens.main.DetailScreen
import com.hyosakura.imagehub.ui.screens.main.MainScreen
import com.hyosakura.imagehub.ui.screens.search.SearchResultsScreen
import com.hyosakura.imagehub.ui.screens.search.SearchScreen
import com.hyosakura.imagehub.viewmodel.DeviceImageViewModel
import com.hyosakura.imagehub.viewmodel.DeviceImageViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    repository: DataRepository
) {
    val allScreens = values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = Screen.fromRoute(backstackEntry.value?.destination?.route)

    val deviceImageViewModel: DeviceImageViewModel = viewModel(factory = DeviceImageViewModelFactory(repository))
    val deviceImageList by DeviceImageViewModel.imageList.observeAsState()

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
                    SearchScreen(repository, onSearchBarClick = { navController.navigate(SearchResults.name) })
                }
                composable(Library.name) {
                    LibraryScreen(navController, deviceImageList)
                }
                composable(SearchResults.name) {
                    SearchResultsScreen(repository, navController)
                }
                composable(Tag.name) {
                    TagScreen(repository, navController)
                }
                composable(Folder.name) {
                    FolderScreen(repository, null)
                }
                composable("${Folder.name}/{folderId}",
                    arguments = listOf(navArgument("folderId") { type = NavType.IntType })
                ) {
                    FolderScreen(repository, it.arguments?.getInt("folderId"))
                }
                composable(Tip.name) {
                    TipScreen()
                }
                composable(Trash.name) {
                    TrashScreen(repository, navController)
                }
                composable(
                    "${Detail.name}/{imageId}",
                    arguments = listOf(navArgument("imageId") { type = NavType.IntType })
                ) {
                    DetailScreen(it.arguments?.getInt("imageId"), repository, navController)
                }
                composable("${TagImage.name}/{tagId}",
                    arguments = listOf(navArgument("tagId") { type = NavType.IntType })
                ) {
                    TagImageScreen(repository, it.arguments?.getInt("tagId"), navController)
                }
                composable("${AddDeviceImage.name}/{imageId}",
                    arguments = listOf(navArgument("imageId") { type = NavType.IntType })) {
                    ImportDeviceImageScreen(repository, it.arguments?.getInt("imageId"), navController)
                }

            }
        },
        bottomBar = {
            BottomBar(navController, currentScreen)
        }
    )

    LaunchedEffect(LocalContext.current) {
        CoroutineScope(Dispatchers.IO).launch {
            deviceImageViewModel.getDeviceImage()
        }
    }
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
            style = MaterialTheme.typography.headlineMedium
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