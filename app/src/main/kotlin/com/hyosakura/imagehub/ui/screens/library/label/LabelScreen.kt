package com.hyosakura.imagehub.ui.screens.library.label

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hyosakura.imagehub.R

/*
    实现编辑标签文本输入框
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelScreen() {

    val labelList = remember { mutableStateListOf("Test1", "Test2", "Test3") }
    var isEditMode by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf("Test") }
    var isAddMode by remember { mutableStateOf(false) }


    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text("标签列表") },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "标签列表"
                        )
                    }
                }
            )
        })
    {
        Column(Modifier.fillMaxSize()) {
            for (label in labelList) {
                LabelItem(label) { isEditMode = true }
            }
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val fab = createRef()
                ExtendedFloatingActionButton(
                    onClick = { isAddMode = !isAddMode },
                    icon = { Icon(Icons.Filled.Add, "添加标签") },
                    text = { Text(text = "添加标签") },
                    modifier = Modifier.constrainAs(fab) {

                        bottom.linkTo(parent.bottom, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                    }
                )
            }
        }
    }

    if (isEditMode) {
        AlertDialog(
            onDismissRequest = {
                isEditMode = false
            },
            title = {
                Text(text = "编辑标签")
            },
            text = {
                OutlinedTextField(
                    value = editText, onValueChange = {
                        editText = it
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.inversePrimary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isEditMode = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isEditMode = false
                    }
                ) {
                    Text("取消")
                }
            }
        )
    }

    if (isAddMode) {
        AlertDialog(
            onDismissRequest = {
                isAddMode = false
            },
            title = {
                Text(text = "添加标签")
            },
            text = {
                OutlinedTextField(
                    value = editText, onValueChange = {
                        editText = it
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.inversePrimary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isAddMode = false
                        labelList.add(editText)
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isAddMode = false
                    }
                ) {
                    Text("取消")
                }
            }
        )

    }

}

@Composable
private fun LabelItem(label: String, onEditClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        var isStar by remember { mutableStateOf(false) }

        TextButton(onClick = { isStar = !isStar }, modifier = Modifier.requiredSize(60.dp))
        {
            if (isStar) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_label_24),
                    contentDescription = null
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_label_24),
                    contentDescription = null
                )
            }
        }

        TextButton(
            onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(label, style = MaterialTheme.typography.titleMedium)
            }
        }

        TextButton(
            onClick = onEditClick , modifier = Modifier
                .requiredSize(60.dp)
                .offset((-30).dp, 0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun LabelItemPreview() {
    LabelItem("Test") {  }
}