package com.micrantha.skouter.data.account.model

import com.micrantha.skouter.domain.model.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CurrentSession {
    private val response = MutableStateFlow<Session?>(null)

    fun update(value: Session) {
        response.update { value }
    }

    fun requirePlayer() = response.value!!.player

    fun requireAccessToken() = response.value!!.accessToken

    fun requireRefreshToken() = response.value!!.refreshToken

    fun asStateFlow() = response.asStateFlow()
}
