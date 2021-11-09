package com.hyosakura.imagehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hyosakura.imagehub.ui.BasePage
import com.hyosakura.imagehub.ui.theme.ImageHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageHubTheme {
                BasePage()
            }
        }
    }
}
