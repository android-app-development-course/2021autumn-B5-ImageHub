package com.hyosakura.imagehub.util

import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.util.ToastUtil.long
import com.hyosakura.imagehub.util.ToastUtil.short
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Paths


object ImageUtil {
    private val options = BitmapFactory.Options()

    fun decodeFile(url: String, size: Int): Bitmap {
        // 输出参数
        options.inSampleSize = size
        return BitmapFactory.decodeFile(url, options)
    }

    fun getThumbnail(url: String): Bitmap {
        val size = File(url).length()
        return decodeFile(
            url,
            if (size > 1024 * 500) 25 else 5
        )
    }

    fun Context.getImageFromShare(bitmap: Bitmap) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        val values = ContentValues()
        val cr = this.contentResolver
        values.put(MediaStore.Images.Media.TITLE, "IMG:${System.currentTimeMillis()}")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val imageOut = cr.openOutputStream(url!!)
        imageOut.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, url)
        this.startActivity(Intent.createChooser(intent, "来自ImageHub的分享"))
    }

    private fun getFilePathFromContentUri(
        selectedVideoUri: Uri?,
        contentResolver: ContentResolver
    ): String? {
        val filePath: String
        val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = contentResolver.query(selectedVideoUri!!, filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        filePath = cursor.getString(columnIndex)
        cursor.close()
        return filePath
    }

    fun saveImageToLocalStorge(
        context: Context,
        bitmap: Bitmap,
        filename: String,
    ): String? {
        val f = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename
        )
        try {
            val fos = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return f.absolutePath
    }

    fun saveBitmapToMedia(context: Context, fileName: String, bitmap: Bitmap) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        try {
            val uri: Uri =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: return
            val path = getFilePathFromContentUri(uri, context.contentResolver)!!
            val dir = File(File(path).parentFile, "ImageHub")
            dir.mkdirs()
            val file = File(dir, fileName)
            val out: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            context.long("图片成功保存至${Paths.get(File(dir.parent!!).parent).relativize(Paths.get(file.absolutePath))}")
        } catch (e: IOException) {
            e.printStackTrace()
            context.short("图片保存失败")
        }
    }

    fun copyImage(image: ImageEntity, context: Context) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val copyUri = Uri.parse(image.url)
        val clip = ClipData.newUri(context.contentResolver, "URI", copyUri)
        clipboardManager?.setPrimaryClip(clip)
        context.short("图片已复制到剪贴板")
    }
}