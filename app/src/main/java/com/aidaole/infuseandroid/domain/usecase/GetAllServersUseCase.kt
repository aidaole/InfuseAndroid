package com.aidaole.infuseandroid.domain.usecase

import com.aidaole.infuseandroid.data.repository.ServerRepository
import com.aidaole.infuseandroid.domain.model.ServerInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllServersUseCase @Inject constructor(
    private val serverRepository: ServerRepository
) {
    operator fun invoke(): Flow<List<ServerInfo>> {
        return serverRepository.getAllServers()
    }
} 