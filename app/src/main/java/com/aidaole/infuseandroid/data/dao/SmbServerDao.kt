package com.aidaole.infuseandroid.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aidaole.infuseandroid.data.entity.SmbServerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SmbServerDao {
    @Query("SELECT * FROM smb_servers")
    fun getAllServers(): Flow<List<SmbServerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(server: SmbServerEntity)

    @Query("DELETE FROM smb_servers WHERE id = :serverId")
    suspend fun deleteServer(serverId: String)

    @Query("UPDATE smb_servers SET isConnected = :isConnected WHERE id = :serverId")
    suspend fun updateServerConnection(serverId: String, isConnected: Boolean)

    @Query("SELECT * FROM smb_servers WHERE id = :serverId")
    suspend fun findServer(serverId: String): SmbServerEntity
} 