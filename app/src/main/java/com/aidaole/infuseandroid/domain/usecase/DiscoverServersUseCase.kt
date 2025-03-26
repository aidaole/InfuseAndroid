package com.aidaole.infuseandroid.domain.usecase

import com.aidaole.infuseandroid.data.repository.ServerRepository
import com.aidaole.infuseandroid.domain.model.ServerInfo
import javax.inject.Inject

class DiscoverServersUseCase @Inject constructor(
    private val serverRepository: ServerRepository
) {
    suspend operator fun invoke(): List<ServerInfo> {
        return serverRepository.discoverServers()
    }
} 