package com.hyosakura.imagehub.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class BasePageState {
    var selectedItem by mutableStateOf(0)
}