package com.hyosakura.imagehub.ui.screens.main

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun MainScreen() {
    Column {
        ImageListWithDate(
            "2020/01/01", listOf(
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
                Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565),
            )
        )
    }
}