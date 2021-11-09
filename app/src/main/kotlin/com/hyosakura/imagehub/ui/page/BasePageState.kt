package com.hyosakura.imagehub.ui.page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class BasePageState {
    var currentPage by mutableStateOf(0)
}