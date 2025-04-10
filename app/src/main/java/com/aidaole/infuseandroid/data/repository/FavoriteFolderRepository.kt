package com.aidaole.infuseandroid.data.repository

import com.aidaole.infuseandroid.data.entity.FavoriteFolderEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteFolderRepository {

    fun getAllFavoriteFolders(): Flow<List<FavoriteFolderEntity>>

    suspend fun addFavoriteFolder(folder: FavoriteFolderEntity)

    suspend fun removeFavoriteFolder(folderId: Long)
}
