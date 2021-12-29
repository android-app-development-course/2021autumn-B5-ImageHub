package com.hyosakura.imagehub.util

import android.app.Activity
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hyosakura.imagehub.dao.FolderDao
import com.hyosakura.imagehub.dao.HistoryDao
import com.hyosakura.imagehub.dao.ImageDao
import com.hyosakura.imagehub.dao.TagDao
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.HistoryEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Database(
    entities = [ImageEntity::class, FolderEntity::class, TagEntity::class, ImageTagCrossRef::class, HistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun folderDao(): FolderDao
    abstract fun tagDao(): TagDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private fun Long.toLocalDateTime(): LocalDateTime {
            val instant = Instant.ofEpochMilli(this)
            val zone = ZoneId.systemDefault()
            return LocalDateTime.ofInstant(instant, zone)
        }

        fun getDatabase(application: Activity): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application.applicationContext,
                    AppDatabase::class.java,
                    "imagehub",
                )
                    .addCallback(object : Callback() {
                        private val scope = CoroutineScope(SupervisorJob())
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            scope.launch(Dispatchers.IO) {
                                INSTANCE
                                    ?.folderDao()
                                    ?.insertFolders(FolderEntity(folderId = -1, parentId = -2, name = "无文件夹"))
                                INSTANCE?.imageDao()?.getAllDeletedImages()?.collect { list->
                                    list.forEach {
                                        val deleteTime = it.deleteTime!!.toLocalDateTime()
                                        val current = System.currentTimeMillis().toLocalDateTime()
                                        if (Duration.between(deleteTime, current).toDays() >= 30) {
                                            INSTANCE?.imageDao()?.removeDeletedImages(it)
                                        }
                                    }
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

