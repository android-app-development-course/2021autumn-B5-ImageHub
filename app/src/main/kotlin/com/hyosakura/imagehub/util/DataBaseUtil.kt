package com.hyosakura.imagehub.util

import android.app.Activity
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hyosakura.imagehub.dao.DirDao
import com.hyosakura.imagehub.dao.ImageDao
import com.hyosakura.imagehub.dao.TagDao
import com.hyosakura.imagehub.entity.FolderEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [ImageEntity::class, FolderEntity::class, TagEntity::class, ImageTagCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun dirDao(): DirDao
    abstract fun tagDao(): TagDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

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
                                    ?.dirDao()
                                    ?.insertDirs(FolderEntity(folderId = -1, parentId = -2, name = "无文件夹"))
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

