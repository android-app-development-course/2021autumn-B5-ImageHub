package com.hyosakura.imagehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hyosakura.imagehub.ui.BaseScreen
import com.hyosakura.imagehub.ui.theme.ImageHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageHubTheme {
                BaseScreen()
            }
        }
    }
}
