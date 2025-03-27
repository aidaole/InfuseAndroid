package com.aidaole.infuseandroid.smb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aidaole.infuseandroid.domain.model.SmbServer
import com.aidaole.infuseandroid.domain.repository.SmbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmbServiceViewModel @Inject constructor(
    private val repository: SmbRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SmbServiceUiState>(SmbServiceUiState.Initial)
    val uiState: StateFlow<SmbServiceUiState> = _uiState.asStateFlow()

    private val _servers = MutableStateFlow<List<SmbServer>>(emptyList())
    val servers: StateFlow<List<SmbServer>> = _servers.asStateFlow()

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

    fun connectToServer(server: SmbServer) {
        viewModelScope.launch {
            _uiState.value = SmbServiceUiState.Loading
            try {
                val success = repository.connectToServer(server)
                _uiState.value = if (success) {
                    SmbServiceUiState.Success
                } else {
                    SmbServiceUiState.Error("Failed to connect to server")
                }
            } catch (e: Exception) {
                _uiState.value = SmbServiceUiState.Error(e.message ?: "Failed to connect to server")
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
}

sealed class SmbServiceUiState {
    object Initial : SmbServiceUiState()
    object Loading : SmbServiceUiState()
    object Success : SmbServiceUiState()
    data class Error(val message: String) : SmbServiceUiState()
} 