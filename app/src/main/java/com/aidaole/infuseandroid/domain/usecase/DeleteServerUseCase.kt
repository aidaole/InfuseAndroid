package com.aidaole.infuseandroid.domain.usecase

import com.aidaole.infuseandroid.data.repository.ServerRepository
import com.aidaole.infuseandroid.domain.model.ServerInfo
import javax.inject.Inject

class DeleteServerUseCase @Inject constructor(
    private val serverRepository: ServerRepository
) {
    suspend operator fun invoke(server: ServerInfo) {
        serverRepository.deleteServer(server)
    }
} 