package com.hyosakura.imagehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.BaseScreen
import com.hyosakura.imagehub.ui.theme.ImageHubTheme
import com.hyosakura.imagehub.util.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainActivity : ComponentActivity() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this) }

    val repository by lazy { DataRepository(database) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageHubTheme {
                BaseScreen(repository)
            }
        }
    }
}
