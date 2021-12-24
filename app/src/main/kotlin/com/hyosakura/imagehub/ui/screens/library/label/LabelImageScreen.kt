package com.hyosakura.imagehub.ui.screens.library.label

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LabelImageScreen(labelId: Int?) {
    Text(text = labelId.toString())
    // TODO: 通过 ID 获取标签所有的图片实体列表，并把列表传给 ImageListWithDate 显示
}