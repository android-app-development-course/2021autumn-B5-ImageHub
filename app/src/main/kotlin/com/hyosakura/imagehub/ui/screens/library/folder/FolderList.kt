package com.hyosakura.imagehub.ui.screens.library.folder

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyosakura.imagehub.R
import com.hyosakura.imagehub.repository.DataRepository
import com.hyosakura.imagehub.ui.screens.main.ImageListWithDate
import com.hyosakura.imagehub.viewmodel.DirManageViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderList(
    repository: DataRepository,
    viewModel: DirManageViewModel
) {
    // 文件夹显示 todo (可单独滚动)
    Row {
        viewModel.currentChildDir.observeAsState().value?.let {
            Log.i("list", it.toString())
            Log.i("indices", it.indices.toString())
            for (i in it.indices step 2) {
                Log.i("i", i.toString())
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.weight(0.5F)) {
                        val entity = it[i]
                        Log.i("first", entity.toString())
                        // todo 图片未正常显示
                        if (entity.latestPicture == null) {
                            Image(
                                painterResource(R.drawable.ic_outline_folder_24),
                                null,
                                Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                entity.latestPicture!!.asImageBitmap(),
                                null,
                                Modifier.size(120.dp)
                            )
                        }
                    }
                    if ((i + 1) in it.indices) {
                        Column(Modifier.weight(0.5F)) {
                            val entity = it[i + 1]
                            Log.i("second", entity.toString())
                            if (entity.latestPicture == null) {
                                Image(
                                    painterResource(R.drawable.ic_outline_folder_24),
                                    null,
                                    Modifier.size(120.dp)
                                )
                            } else {
                                Image(
                                    entity.latestPicture!!.asImageBitmap(),
                                    null,
                                    Modifier.size(120.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    // todo 内容分割(不动点)

    // 图片显示 todo (可单独滚动)
    Row {
        viewModel.imagesInCurrentDir.observeAsState().value?.let {
            val images = it.map { entity ->
                entity.bitmap!!
            }
            for (i in it.indices) {
                ImageItem(images, i)
            }
        }
    }

}

@Composable
private fun ImageItem(images: List<Bitmap>, image: Int) {
    Image(
        images[image].asImageBitmap(), null,
        Modifier
            .size(120.dp)
            .clickable { })
}


@Preview
@Composable
fun PictureListWithDatePreview() {

}