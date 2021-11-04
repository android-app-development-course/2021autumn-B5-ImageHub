package com.hyosakura.imagehub

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.DirWithImage
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import com.hyosakura.imagehub.util.AppDatabase
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "imagehub"
        ).build()
        val qq = ImageEntity(1, 1,"图片", "QQ", "jpg")
        val wechat = ImageEntity(2, 1,"图片", "微信", "jpg")
        db.imageDao().insertImages(qq)
        db.imageDao().insertImages(wechat)

        val javaTag = TagEntity(1, "java")
        val jsTag = TagEntity(2, "js")
        db.tagDao().insertTags(javaTag, jsTag)

        val learning = DirEntity(1, -1, "学习")
        val programing = DirEntity(2, 1, "编程")
        db.dirDao().insertDirs(learning, programing)

        db.tagDao().insertImages(ImageTagCrossRef(1, 1))
        db.tagDao().insertImages(ImageTagCrossRef(1, 2))

        db.imageDao().insertTags(ImageTagCrossRef(2, 1))
        db.imageDao().insertTags(ImageTagCrossRef(2, 2))


        println(db.tagDao().getTagWithImagesById(2))
    }
}