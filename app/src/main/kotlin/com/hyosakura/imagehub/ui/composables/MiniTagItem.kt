package com.hyosakura.imagehub.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.entity.TagEntity

@Composable
fun MiniTagItem(tagEntity: TagEntity, onTagClick: () -> Unit) {
    Row(Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = onTagClick,
            modifier = Modifier.padding(1.dp),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text(tagEntity.name!!)
        }
    }
}