package com.aidaole.infuseandroid.ui.screen.servers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidaole.infuseandroid.data.entity.FavoriteFolderEntity
import com.aidaole.infuseandroid.data.model.FileItem
import com.aidaole.infuseandroid.data.model.SmbServer
import com.aidaole.infuseandroid.data.repository.SmbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServersManageViewModel @Inject constructor(
    private val repository: SmbRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SmbServiceUiState>(SmbServiceUiState.Initial)
    val uiState: StateFlow<SmbServiceUiState> = _uiState.asStateFlow()

    private val _servers = MutableStateFlow<List<SmbServer>>(emptyList())
    val servers: StateFlow<List<SmbServer>> = _servers.asStateFlow()

    private val _currentPath = MutableStateFlow<String>("")
    val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files.asStateFlow()

    private val _favoriteFolders = MutableStateFlow<List<FavoriteFolderEntity>>(emptyList())
    val favoriteFolders: StateFlow<List<FavoriteFolderEntity>> = _favoriteFolders.asStateFlow()

    init {
        loadServers()
    }

    private fun loadServers() {
        viewModelScope.launch {
            repository.getServers()
                .catch { e ->
                    _uiState.value = SmbServiceUiState.Error(e.message ?: "Unknown error")
                }
                .collect { serverList ->
                    _servers.value = serverList
                    _uiState.value = SmbServiceUiState.Success
                }
        }
    }

    fun addServer(name: String, host: String, username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = SmbServiceUiState.Loading
            try {
                val server = SmbServer(
                    name = name,
                    host = host,
                    username = username,
                    password = password
                )
                repository.addServer(server)
                _uiState.value = SmbServiceUiState.Success
            } catch (e: Exception) {
                _uiState.value = SmbServiceUiState.Error(e.message ?: "Failed to add server")
            }
        }
    }


    fun removeServer(serverId: String) {
        viewModelScope.launch {
            _uiState.value = SmbServiceUiState.Loading
            try {
                repository.removeServer(serverId)
                _uiState.value = SmbServiceUiState.Success
            } catch (e: Exception) {
                _uiState.value = SmbServiceUiState.Error(e.message ?: "Failed to remove server")
            }
        }
    }

    fun navigateToDirectory(serverId: String, path: String) {
        viewModelScope.launch {
            scanDirectory(serverId, path)
        }
    }

    fun removeFavoriteFolder(id: Long) {

    }

    fun addFavoriteFolder(s: String, path: String, name: String) {

    }

    fun openServer(server: SmbServer) {
        viewModelScope.launch {
            _uiState.value = SmbServiceUiState.Loading
            try {
                val success = repository.connectToServer(server)
                if (success) {
                    _uiState.value = SmbServiceUiState.Success
                    // 连接成功后自动扫描根目录
                    scanDirectory(server.id, "/")
                } else {
                    _uiState.value = SmbServiceUiState.Error("Failed to connect to server")
                }
            } catch (e: Exception) {
                _uiState.value = SmbServiceUiState.Error(e.message ?: "Failed to connect to server")
            }
        }
    }

    private fun scanDirectory(serverId: String, path: String) {
        viewModelScope.launch {
            _uiState.value = SmbServiceUiState.Loading
            try {
                val items = repository.scanDirectory(serverId, path)
                _files.value = items
                _currentPath.value = path
                _uiState.value = SmbServiceUiState.Success
            } catch (e: Exception) {
                _uiState.value = SmbServiceUiState.Error(e.message ?: "Failed to scan directory")
            }
        }
    }
}

sealed class SmbServiceUiState {
    object Initial : SmbServiceUiState()
    object Loading : SmbServiceUiState()
    object Success : SmbServiceUiState()
    data class Error(val message: String) : SmbServiceUiState()
} 