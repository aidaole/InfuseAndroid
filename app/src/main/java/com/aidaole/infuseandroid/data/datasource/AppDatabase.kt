package com.aidaole.infuseandroid.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aidaole.infuseandroid.data.datasource.dao.ServerDao
import com.aidaole.infuseandroid.data.model.ServerEntity

@Database(
    entities = [ServerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
} 