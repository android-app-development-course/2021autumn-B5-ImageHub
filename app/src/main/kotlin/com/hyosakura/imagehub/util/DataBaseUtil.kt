package com.hyosakura.imagehub.util

import androidx.activity.ComponentActivity
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hyosakura.imagehub.dao.DirDao
import com.hyosakura.imagehub.dao.ImageDao
import com.hyosakura.imagehub.dao.TagDao
import com.hyosakura.imagehub.entity.DirEntity
import com.hyosakura.imagehub.entity.ImageEntity
import com.hyosakura.imagehub.entity.TagEntity
import com.hyosakura.imagehub.entity.relation.ImageTagCrossRef

@Database(
    entities = [ImageEntity::class, DirEntity::class, TagEntity::class, ImageTagCrossRef::class],
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

        fun getDatabase(application: ComponentActivity): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application.applicationContext,
                    AppDatabase::class.java,
                    "imagehub"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

object DataBaseUtil {
    lateinit var db: AppDatabase
}
