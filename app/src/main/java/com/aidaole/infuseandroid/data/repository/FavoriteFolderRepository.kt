package com.aidaole.infuseandroid.data.repository

import com.aidaole.infuseandroid.data.model.FavoriteFolder
import kotlinx.coroutines.flow.Flow

interface FavoriteFolderRepository {

    fun getAllFavoriteFolders(): Flow<List<FavoriteFolder>>

    suspend fun addFavoriteFolder(folder: FavoriteFolder)

    suspend fun removeFavoriteFolder(folderId: Long)
}
