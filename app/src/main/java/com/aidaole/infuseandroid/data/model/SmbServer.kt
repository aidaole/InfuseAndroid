package com.aidaole.infuseandroid.data.model

data class SmbServer(
    val id: String = "",
    val name: String,
    val host: String,
    val username: String,
    val password: String,
    val isConnected: Boolean = false
) 