package com.hyosakura.imagehub.ui.screens.library.folder

import androidx.compose.runtime.Composable
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.viewmodel.DirManageViewModel
import com.hyosakura.imagehub.viewmodel.DirManageViewModelFactory

@Composable
fun FolderScreen(
    repository: DataRepository,
    viewModel: DirManageViewModel = DirManageViewModelFactory(repository).create(DirManageViewModel::class.java)
) {
    FolderList(repository, viewModel)
}