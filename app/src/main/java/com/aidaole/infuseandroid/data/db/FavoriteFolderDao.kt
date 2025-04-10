package com.aidaole.infuseandroid.data.db

import androidx.room.*
import com.aidaole.infuseandroid.data.entity.FavoriteFolderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFolderDao {
    @Query("SELECT * FROM favorite_folders ORDER BY lastModified DESC")
    fun getAllFavoriteFolders(): Flow<List<FavoriteFolderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteFolder(folder: FavoriteFolderEntity)

    @Delete
    suspend fun deleteFavoriteFolder(folder: FavoriteFolderEntity)

    @Query("DELETE FROM favorite_folders WHERE id = :folderId")
    suspend fun deleteFavoriteFolderById(folderId: Long)
} 