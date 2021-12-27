package com.hyosakura.imagehub.ui

import android.widget.Toast
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
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.Screen
import com.hyosakura.imagehub.ui.screens.Screen.*
import com.hyosakura.imagehub.ui.screens.library.ImportDeviceImageScreen
import com.hyosakura.imagehub.ui.screens.library.LibraryScreen
import com.hyosakura.imagehub.ui.screens.library.folder.FolderChooseScreen
import com.hyosakura.imagehub.ui.screens.library.folder.FolderScreen
import com.hyosakura.imagehub.ui.screens.library.tag.TagImageScreen
import com.hyosakura.imagehub.ui.screens.library.tag.TagScreen
import com.hyosakura.imagehub.ui.screens.library.tip.TipScreen
import com.hyosakura.imagehub.ui.screens.library.trash.RecycleBinScreen
import com.hyosakura.imagehub.ui.screens.main.DetailScreen
import com.hyosakura.imagehub.ui.screens.main.MainScreen
import com.hyosakura.imagehub.ui.screens.search.SearchResultsScreen
import com.hyosakura.imagehub.ui.screens.search.SearchScreen
import com.hyosakura.imagehub.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val coroutine = CoroutineScope(Dispatchers.IO)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    repository: DataRepository,
    imageManageViewModel: ImageManageViewModel = viewModel(
        factory = ImageManageViewModelFactory(
            repository
        )
    ),
    folderManageViewModel: FolderManageViewModel = viewModel(
        factory = FolderManageViewModelFactory(
            repository
        )
    ),
    tagManageViewModel: TagManageViewModel = viewModel(
        factory = TagManageViewModelFactory(
            repository
        )
    ),
    recycleBinViewModel: RecycleBinViewModel = viewModel(
        factory = RecycleBinViewModelFactory(
            repository
        )
    ),
    deviceImageManageViewModel: DeviceImageViewModel = viewModel(
        factory = DeviceImageViewModelFactory(
            repository
        )
    )
) {
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = Screen.fromRoute(backstackEntry.value?.destination?.route)
    val context = LocalContext.current

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
                    imageManageViewModel.imageList.observeAsState().value?.let { list ->
                        MainScreen(list) {
                            navController.navigate("${Detail.name}/${imageId}")
                        }
                    }
                }
                composable(Search.name) {
                    val starTags by tagManageViewModel.starTags.observeAsState()
                    val num = 20
                    val recentTags by tagManageViewModel.getRecentTag(num).observeAsState()
                    SearchScreen(
                        tagManageViewModel,
                        starTags,
                        recentTags,
                        onSearchBarClick = {
                            navController.navigate(SearchResults.name)
                        },
                        onSuggestTagClick = { tag -> navController.navigate("${TagImage.name}/${tag.tagId}") }
                    )
                }
                composable(Library.name) {
                    val deviceImageList by DeviceImageViewModel.imageList.observeAsState()
                    LibraryScreen(
                        onTagButtonClick = {
                            navController.navigate(Tag.name)
                        },
                        onFolderButtonClick = {
                            navController.navigate(Folder.name)
                        },
                        onTipButtonClick = {
                            navController.navigate(Tip.name)
                        },
                        onRecycleBinButtonClick = {
                            navController.navigate(RecycleBin.name)
                        },
                        onDeviceImageClick = {
                            navController.navigate("${AddDeviceImage.name}/${imageId}")
                        },
                        deviceImageList
                    )
                }
                composable(SearchResults.name) {
                    val result by imageManageViewModel.imageList.observeAsState()
                    SearchResultsScreen(
                        searchAction = {
                            if (it.isNotBlank()) {
                                imageManageViewModel.searchImage(it)
                            }
                        },
                        result,
                        imageManageViewModel,
                        onImageClick = {
                            navController.navigate("${Detail.name}/${imageId}")
                        }
                    )
                }
                composable(Tag.name) {
                    val allTags by tagManageViewModel.allTags.observeAsState()
                    val candidateTags by tagManageViewModel.candidateTagWithName.observeAsState()
                    TagScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                        allTags,
                        candidateTags,
                        insertAction = {
                            if (it.isNotBlank()) {
                                tagManageViewModel.insertTag(
                                    TagEntity(
                                        name = it,
                                        addTime = System.currentTimeMillis()
                                    )
                                )
                            }
                        },
                        updateAction = {
                            tagManageViewModel.updateTag(this)
                        },
                        deleteAction = {
                            tagManageViewModel.deleteTag(this)
                        },
                        candidateAction = { name ->
                            tagManageViewModel.getTagByName(name, false)
                        },
                        onTagClick = {
                            navController.navigate("${TagImage.name}/${tagId}")
                        },
                        onTagConflict = {
                            coroutine.launch {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "标签已存在",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )
                }
                composable(Folder.name) {
                    folderManageViewModel.visitFolder(-1)
                    val folder: FolderEntity =
                        folderManageViewModel.currentFolder.observeAsState().value ?: FolderEntity()
                    val images by folderManageViewModel.imagesInCurrentFolder.observeAsState()
                    val childFolder by folderManageViewModel.currentChildFolder.observeAsState()
                    FolderScreen(
                        images = images,
                        childFolder = childFolder,
                        folder = folder,
                        onBack = {
                            navController.popBackStack()
                        },
                        onFolderClick = { folderId ->
                            navController.navigate("${Folder.name}/${folderId}")
                        },
                        onFolderAdd = {
                            coroutine.launch {
                                folderManageViewModel.newFolder(it)
                            }
                        },
                        onImageClick = {
                            navController.navigate("${Detail.name}/${imageId}")
                        }
                    )
                }
                composable(
                    "${Folder.name}/{folderId}",
                    arguments = listOf(navArgument("folderId") { type = NavType.IntType })
                ) {
                    folderManageViewModel.visitFolder(it.arguments?.getInt("folderId") ?: -1)
                    val folder: FolderEntity =
                        folderManageViewModel.currentFolder.observeAsState().value ?: FolderEntity()
                    val images by folderManageViewModel.imagesInCurrentFolder.observeAsState()
                    val childFolder by folderManageViewModel.currentChildFolder.observeAsState()
                    FolderScreen(
                        images = images,
                        childFolder = childFolder,
                        folder = folder,
                        onBack = {
                            navController.popBackStack()
                        },
                        onFolderClick = { folderId ->
                            navController.navigate("${Folder.name}/${folderId}")
                        },
                        onFolderAdd = {
                            coroutine.launch {
                                folderManageViewModel.newFolder(it)
                            }
                        },
                        onImageClick = { navController.navigate("${Detail.name}/${imageId}") }
                    )
                }
                composable(
                    "${FolderChooseScreen.name}/{imageId}/{folderId}",
                    arguments = listOf(
                        navArgument("imageId") { type = NavType.IntType },
                        navArgument("folderId") { type = NavType.IntType }
                    )
                ) {
                    folderManageViewModel.visitFolder(it.arguments?.getInt("folderId") ?: -1)
                    val folder: FolderEntity =
                        folderManageViewModel.currentFolder.observeAsState().value ?: FolderEntity()
                    val images by folderManageViewModel.imagesInCurrentFolder.observeAsState()
                    val childFolder by folderManageViewModel.currentChildFolder.observeAsState()
                    val imageId = it.arguments?.getInt("imageId")!!
                    imageManageViewModel.visitImage(imageId)
                    val image by imageManageViewModel.image.observeAsState()
                    val folderById by folderManageViewModel.folderById.observeAsState()
                    FolderChooseScreen(
                        images = images,
                        childFolder = childFolder,
                        folder = folder,
                        onBack = {
                            navController.popBackStack()
                        },
                        onFolderClick = { folderId ->
                            navController.navigate("${FolderChooseScreen.name}/${folderId}")
                        },
                        onFolderAdd = {
                            coroutine.launch {
                                val id = folderManageViewModel.newFolderAndGetId(it)
                                folderManageViewModel.getFolderById(id)
                                folderById?.let { f ->
                                    folderManageViewModel.moveFolder(f, folder)
                                }
                            }
                        },
                        onChooseClick = { f ->
                            image?.let { i ->
                                imageManageViewModel.addImageToFolder(listOf(i), f)
                            }
                        }
                    )
                }
                composable(Tip.name) {
                    TipScreen()
                }
                composable(RecycleBin.name) {
                    val deletedImages by recycleBinViewModel.allDeletedImages.observeAsState()
                    RecycleBinScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                        deletedImages,
                        onImageClick = {
                            navController.navigate("${Detail.name}/${imageId}")
                        }
                    )
                }
                composable(
                    "${Detail.name}/{imageId}",
                    arguments = listOf(navArgument("imageId") { type = NavType.IntType })
                ) {
                    val imageId = it.arguments?.getInt("imageId")
                    imageManageViewModel.visitImage(imageId!!)
                    val image = imageManageViewModel.image.observeAsState().value ?: ImageEntity()
                    val folder =
                        folderManageViewModel.currentFolder.observeAsState().value ?: FolderEntity()
                    val tagList = imageManageViewModel.tagList.observeAsState().value ?: listOf()
                    val starTags = tagManageViewModel.starTags.observeAsState().value ?: listOf()
                    val num = 20
                    val recentTags =
                        tagManageViewModel.getRecentTag(num).observeAsState().value ?: listOf()
                    val candidateTags =
                        tagManageViewModel.candidateTagWithName.observeAsState().value ?: listOf()
                    DetailScreen(
                        image,
                        folder.name,
                        tagList,
                        starTags,
                        recentTags,
                        candidateTags,
                        onBack = {
                            navController.popBackStack()
                        },
                        onTagClick = {
                            navController.navigate("${TagImage.name}/${tagId}")
                        },
                        onTagInsert = {
                            tagManageViewModel.insertTagAndGetId(this).first().toInt()
                        },
                        onTagDelete = {
                            imageManageViewModel.removeTag(image, this)
                        },
                        onImageDelete = {
                            navController.popBackStack()
                            image.deleted = 1
                            imageManageViewModel.updateImage(image)
                        },
                        onImageRestore = {
                            navController.popBackStack()
                            image.deleted = 0
                            imageManageViewModel.updateImage(image)
                        },
                        onTagAddToImage = { tag ->
                            imageManageViewModel.addTagToImage(image, tag)
                        },
                        onFolderClick = {
                            navController.navigate("${FolderChooseScreen.name}/${image.imageId}/${folder.folderId}")
                        },
                        onAnnotationEdit = { editText ->
                            image.annotation = editText
                            imageManageViewModel.updateImage(image)
                        },
                        candidateAction = { name ->
                            if (name.isNotBlank()) {
                                tagManageViewModel.getTagByName(name, false)
                            }
                        },
                        onTagConflict = {
                            coroutine.launch {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "标签已存在",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )
                }
                composable(
                    "${TagImage.name}/{tagId}",
                    arguments = listOf(navArgument("tagId") { type = NavType.IntType })
                ) {
                    val id = it.arguments?.getInt("tagId")!!
                    val tag by tagManageViewModel.visitTag(id).observeAsState()
                    val imageInTag by tagManageViewModel.getImageInTag(id).observeAsState()
                    TagImageScreen(
                        tag,
                        imageInTag,
                        onBack = {
                            navController.popBackStack()
                        },
                        onImageClick = {
                            navController.navigate("${Detail.name}/${imageId}")
                        }
                    )
                }
                composable(
                    "${AddDeviceImage.name}/{imageId}",
                    arguments = listOf(navArgument("imageId") { type = NavType.IntType })
                ) {
                    val image =
                        deviceImageManageViewModel.getImageById(it.arguments?.getInt("imageId")!!)
                    val starTags = tagManageViewModel.starTags.observeAsState().value ?: listOf()
                    val num = 20
                    val recentTags =
                        tagManageViewModel.getRecentTag(num).observeAsState().value ?: listOf()
                    ImportDeviceImageScreen(
                        image,
                        starTags,
                        recentTags,
                        onBack = {
                            navController.popBackStack()
                        },
                        onImageImport = {
                            coroutine.launch {
                                withContext(Dispatchers.Main) {
                                    val id = deviceImageManageViewModel.importImage(image!!)
                                    navController.popBackStack()
                                    navController.navigate("${Detail.name}/$id")
                                }
                            }
                        }
                    )
                }

            }
        },
        bottomBar = {
            BottomBar(navController, currentScreen)
        }
    )

    LaunchedEffect(LocalContext.current) {
        CoroutineScope(Dispatchers.IO).launch {
            deviceImageManageViewModel.getDeviceImage()
        }
    }
}

@Composable
private fun TopBar(currentScreen: Screen) {
    if(currentScreen == Main || currentScreen == Search || currentScreen == Library) {
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
    if (
        currentScreen == Main || currentScreen == Search || currentScreen == Library
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