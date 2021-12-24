package com.hyosakura.imagehub.ui.screens.library.folder

import androidx.compose.runtime.Composable
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.viewmodel.DirManageViewModel
import com.hyosakura.imagehub.viewmodel.DirManageViewModelFactory

@Composable
fun FolderScreen(
    repository: DataRepository,
    folderId: Int?
) {
    // TODO：根据文件夹 id 获取该文件夹的 子文件夹列表、图片列表，id 为空则获取根文件夹
    val viewModel: DirManageViewModel = DirManageViewModelFactory(repository).create(DirManageViewModel::class.java)
    // TODO：将 子文件夹列表、图片列表, 分别以参数的形式传入 FolderList
    FolderList(repository, viewModel)
}