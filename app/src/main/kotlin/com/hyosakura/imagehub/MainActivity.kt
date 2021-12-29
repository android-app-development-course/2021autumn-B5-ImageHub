package com.hyosakura.imagehub

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.BaseScreen
import com.hyosakura.imagehub.ui.theme.ImageHubTheme
import com.hyosakura.imagehub.util.AppDatabase
import com.hyosakura.imagehub.util.ToastUtil.short

class MainActivity : ComponentActivity() {
    private val database by lazy { AppDatabase.getDatabase(this) }

    val repository by lazy { DataRepository(database) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageHubTheme {
                RequestPermissionUsingAccompanist(repository)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissionUsingAccompanist(repository: DataRepository) {
    val permission = Manifest.permission.READ_EXTERNAL_STORAGE

    val permissionState = rememberPermissionState(permission)
    PermissionRequired(
        permissionState = permissionState,
        permissionNotAvailableContent = {
            LocalContext.current.short("权限获取失败!")
            BaseScreen(repository)
        }, permissionNotGrantedContent = {
            LaunchedEffect(repository) {
                permissionState.launchPermissionRequest()
            }
        }, content = {
            BaseScreen(repository)
        }
    )
}