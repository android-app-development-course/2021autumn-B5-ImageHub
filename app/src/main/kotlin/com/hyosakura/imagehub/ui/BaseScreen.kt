package com.hyosakura.imagehub.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.hyosakura.imagehub.entity.HistoryEntity
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
import com.hyosakura.imagehub.util.ImageUtil
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
    ),
    searchHistoryManageViewModel: HistoryManageViewModel = viewModel(
        factory = HistoryManageViewModelFactory(
            repository
        )
    ),
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
                    val images by imageManageViewModel.allImages.collectAsState(listOf())
                    MainScreen(images) {
                        navController.navigate("${Detail.name}/${imageId}")
                    }
                }
                composable(Search.name) {
                    val starTags by tagManageViewModel.starTags.collectAsState(listOf())
                    val recentTags by tagManageViewModel.recentTags.collectAsState(listOf())
                    val searchHistories by searchHistoryManageViewModel.searchHistories.collectAsState(
                        listOf()
                    )
                    SearchScreen(
                        starTags,
                        recentTags,
                        searchHistories,
                        onSearchBarClick = {
                            navController.navigate(SearchResults.name)
                        },
                        onSuggestTagClick = { tag -> navController.navigate("${TagImage.name}/${tag.tagId}") },
                        // TODO: 搜索历史字符串不能重复； 跳转到搜索结果页无结果；
                        onSearchHistoryClick = {
                                keyword -> imageManageViewModel.searchImage(keyword)
                                navController.navigate(SearchResults.name)
                        }
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
                    val result by imageManageViewModel.searchResult.collectAsState(listOf())
                    val searchHistories by searchHistoryManageViewModel.searchHistories.collectAsState(
                        listOf()
                    )
                    SearchResultsScreen(
                        searchAction = {
                            if (it.isNotBlank()) {
                                imageManageViewModel.searchImage(it)
                                if (
                                    searchHistories.none {e->
                                        e.keyword!! == it
                                    }
                                ) {
                                    searchHistoryManageViewModel.addHistory(HistoryEntity(keyword = it, addTime = System.currentTimeMillis()))
                                }
                            }
                        },
                        result,
                        onImageClick = {
                            navController.navigate("${Detail.name}/${imageId}")
                        }
                    )
                }
                composable(Tag.name) {
                    val allTags by tagManageViewModel.allTags.collectAsState(listOf())
                    val searchResult by tagManageViewModel.candidateTagWithName.collectAsState(listOf())
                    TagScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                        allTags,
                        searchResult,
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
                        },
                        searchAction = {
                            tagManageViewModel.getTagByName(it, true)
                        }
                    )
                }
                composable(Folder.name) {
                    folderManageViewModel.visitFolder(-1)
                    val folder: FolderEntity by
                    folderManageViewModel.currentFolder.collectAsState(FolderEntity())
                    val images by folderManageViewModel.imagesInCurrentFolder.collectAsState(listOf())
                    val childFolder by folderManageViewModel.currentChildFolder.collectAsState(
                        listOf()
                    )
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
                    val folder: FolderEntity by
                    folderManageViewModel.currentFolder.collectAsState(FolderEntity())
                    val images by folderManageViewModel.imagesInCurrentFolder.collectAsState(listOf())
                    val childFolder by folderManageViewModel.currentChildFolder.collectAsState(
                        listOf()
                    )
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
                    val folder: FolderEntity by
                    folderManageViewModel.currentFolder.collectAsState(FolderEntity(-1))
                    val images by folderManageViewModel.imagesInCurrentFolder.collectAsState(listOf())
                    val childFolder by folderManageViewModel.currentChildFolder.collectAsState(
                        listOf()
                    )
                    val imageId = it.arguments?.getInt("imageId")!!
                    imageManageViewModel.visitImage(imageId)
                    val image by imageManageViewModel.image.collectAsState(ImageEntity())
                    val folderById by folderManageViewModel.folderById.collectAsState(FolderEntity(-1))
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
                            image.let { i ->
                                imageManageViewModel.addImageToFolder(listOf(i), f)
                            }
                        }
                    )
                }
                composable(Tip.name) {
                    TipScreen()
                }
                composable(RecycleBin.name) {
                    val deletedImages = recycleBinViewModel.allDeletedImages.observeAsState().value
                    RecycleBinScreen(
                        onBack = {
                            navController.popBackStack()
                        },
                        recycleBinViewModel,
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
                    val image by imageManageViewModel.image.collectAsState(ImageEntity())
                    val folder by folderManageViewModel.currentFolder.collectAsState(FolderEntity())
                    val tagList by imageManageViewModel.tagList.collectAsState(listOf())
                    val starTags by tagManageViewModel.starTags.collectAsState(listOf())
                    val recentTags by tagManageViewModel.recentTags.collectAsState(listOf())
                    val candidateTags by tagManageViewModel.candidateTagWithName.collectAsState(
                        listOf()
                    )
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
                        onCopyClick = {
                            ImageUtil.copyImage(image, context)
                            coroutine.launch {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "图片已复制到剪切板",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        onAnnotationEdit = { editText ->
                            image.annotation = editText
                            imageManageViewModel.updateImage(image)
                        },
                        candidateAction = { name ->
                            if (name.isNotBlank()) {
                                tagManageViewModel.getTagByName(name, true)
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
                    tagManageViewModel.visitTag(id)
                    val tag by tagManageViewModel.tag.collectAsState(TagEntity(id, ""))
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
                    val image = deviceImageManageViewModel.getImageById(it.arguments?.getInt("imageId")!!)
                    val starTags by tagManageViewModel.starTags.collectAsState(listOf())
                    val recentTags by tagManageViewModel.recentTags.collectAsState(listOf())
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
    AnimatedVisibility (currentScreen == Main || currentScreen == Search || currentScreen == Library) {
        BaseTopBar()
    }
}


@Composable
private fun BaseTopBar() {
    CenterAlignedTopAppBar(title = {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(id = R.drawable.ic_icon), contentDescription = null,
                Modifier
                    .size(55.dp)
                    .padding(end = 10.dp))
            Image(painterResource(id = R.drawable.logo_imagehub), contentDescription =null, Modifier.size(150.dp) )
        }
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