package com.aidaole.infuseandroid.di

import android.content.Context
import androidx.room.Room
import com.aidaole.infuseandroid.data.dao.SmbServerDao
import com.aidaole.infuseandroid.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSmbServerDao(database: AppDatabase): SmbServerDao {
        return database.smbServerDao()
    }
} 