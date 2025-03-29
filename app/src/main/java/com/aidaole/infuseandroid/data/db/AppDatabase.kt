package com.aidaole.infuseandroid.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aidaole.infuseandroid.data.dao.SmbServerDao
import com.aidaole.infuseandroid.data.entity.SmbServerEntity

@Database(entities = [SmbServerEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun smbServerDao(): SmbServerDao

    companion object {
        const val DATABASE_NAME = "infuse_android_db"
    }
} 