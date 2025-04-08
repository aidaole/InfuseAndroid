package com.aidaole.infuseandroid.di

import com.aidaole.infuseandroid.data.repository.SmbRepository
import com.aidaole.infuseandroid.data.repository.SmbRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindSmbRepository(
        repository: SmbRepositoryImpl
    ): SmbRepository
} 