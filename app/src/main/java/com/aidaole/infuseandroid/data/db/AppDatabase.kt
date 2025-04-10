package com.aidaole.infuseandroid.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aidaole.infuseandroid.data.dao.SmbServerDao
import com.aidaole.infuseandroid.data.entity.SmbServerEntity
import com.aidaole.infuseandroid.data.entity.FavoriteFolderEntity

@Database(
    entities = [
        SmbServerEntity::class,
        FavoriteFolderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun smbServerDao(): SmbServerDao
    abstract fun favoriteFolderDao(): FavoriteFolderDao

    companion object {
        const val DATABASE_NAME = "infuse_android_db"
    }
} 