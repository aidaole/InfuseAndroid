package com.aidaole.infuseandroid.data.repository

import com.aidaole.infuseandroid.data.db.FavoriteFolderDao
import com.aidaole.infuseandroid.data.entity.FavoriteFolderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteFolderRepositoryImpl @Inject constructor(
    private val favoriteFolderDao: FavoriteFolderDao
) : FavoriteFolderRepository {

    override fun getAllFavoriteFolders(): Flow<List<FavoriteFolderEntity>> {
        return favoriteFolderDao.getAllFavoriteFolders()
    }

    override suspend fun addFavoriteFolder(folder: FavoriteFolderEntity) {
        favoriteFolderDao.insertFavoriteFolder(folder)
    }

    override suspend fun removeFavoriteFolder(folderId: Long) {
        favoriteFolderDao.deleteFavoriteFolderById(folderId)
    }
} 