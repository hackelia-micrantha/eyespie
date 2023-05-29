package com.micrantha.skouter.data.account.model

import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CurrentSession {
    private val response = MutableStateFlow<Session?>(null)

    fun update(value: Session) {
        response.update { value }
    }

    fun update(value: Location) {
        response.update {
            it?.copy(player = it.player.copy(location = value))
        }
    }

    fun requirePlayer() = response.value!!.player

    fun requireAccessToken() = response.value!!.accessToken

    fun requireRefreshToken() = response.value!!.refreshToken

    fun asStateFlow() = response.asStateFlow()
}
