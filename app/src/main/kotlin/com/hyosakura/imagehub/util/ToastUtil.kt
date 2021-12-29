package com.hyosakura.imagehub.util

import android.content.Context
import android.widget.Toast

object ToastUtil {
    fun Context.short(str: String) {
        Toast.makeText(
            this,
            str,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun Context.long(str: String) {
        Toast.makeText(
            this,
            str,
            Toast.LENGTH_LONG
        ).show()
    }
}