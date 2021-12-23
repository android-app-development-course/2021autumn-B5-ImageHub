package com.hyosakura.imagehub.ui.screens.library.folder

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
    Row {
        viewModel.currentChildDir.observeAsState().value?.let {
            for (i in it.indices step 2) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(0.5F)) {
                        val entity = it[i]
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
                    if ((i + 1) in it.indices) {
                        Column(Modifier.weight(0.5F)) {
                            val entity = it[i + 1]
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

    Row {
        viewModel.imagesInCurrentDir.value?.let {
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
    ImageListWithDate(
        "2020/01/01", listOf(
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),
            Bitmap.createBitmap(354, 354, Bitmap.Config.ARGB_8888),


            )
    )
}