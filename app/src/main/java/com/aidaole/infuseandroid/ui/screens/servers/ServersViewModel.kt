package com.aidaole.infuseandroid.ui.screens.servers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidaole.infuseandroid.domain.model.ServerInfo
import com.aidaole.infuseandroid.domain.usecase.DeleteServerUseCase
import com.aidaole.infuseandroid.domain.usecase.DiscoverServersUseCase
import com.aidaole.infuseandroid.domain.usecase.GetAllServersUseCase
import com.aidaole.infuseandroid.domain.usecase.SaveServerUseCase
import com.aidaole.infuseandroid.domain.usecase.TestServerConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServersViewModel @Inject constructor(
    private val getAllServersUseCase: GetAllServersUseCase,
    private val discoverServersUseCase: DiscoverServersUseCase,
    private val saveServerUseCase: SaveServerUseCase,
    private val deleteServerUseCase: DeleteServerUseCase,
    private val testServerConnectionUseCase: TestServerConnectionUseCase
) : ViewModel() {

    val savedServers: StateFlow<List<ServerInfo>> = getAllServersUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var discoveredServers by mutableStateOf<List<ServerInfo>>(emptyList())
        private set

    var isDiscovering by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun discoverServers() {
        viewModelScope.launch {
            try {
                isDiscovering = true
                errorMessage = null
                discoveredServers = discoverServersUseCase()
            } catch (e: Exception) {
                errorMessage = "发现服务器时出错: ${e.message}"
            } finally {
                isDiscovering = false
            }
        }
    }

    fun saveServer(server: ServerInfo) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                saveServerUseCase(server)
            } catch (e: Exception) {
                errorMessage = "保存服务器时出错: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteServer(server: ServerInfo) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                deleteServerUseCase(server)
            } catch (e: Exception) {
                errorMessage = "删除服务器时出错: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun testConnection(server: ServerInfo, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                val result = testServerConnectionUseCase(server)
                onResult(result)
            } catch (e: Exception) {
                errorMessage = "测试连接时出错: ${e.message}"
                onResult(false)
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
} 