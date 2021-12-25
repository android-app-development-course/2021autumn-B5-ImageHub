package com.hyosakura.imagehub.ui.screens.library.folder

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.viewmodel.DirManageViewModel
import com.hyosakura.imagehub.viewmodel.DirManageViewModelFactory

@Composable
fun FolderScreen(
    repository: DataRepository,
    folderId: Int?,
    viewModel: DirManageViewModel = viewModel(factory = DirManageViewModelFactory(repository))
) {
    viewModel.visitDir(folderId ?: -1)
    FolderList(viewModel)
}