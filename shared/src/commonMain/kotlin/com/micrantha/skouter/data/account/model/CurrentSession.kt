package com.micrantha.skouter.data.account.model

import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.Player
import com.micrantha.skouter.domain.model.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CurrentSession {
    private val data = MutableStateFlow<Session?>(null)

    fun update(value: Session) {
        data.update { value }
    }

    fun update(value: Location) {
        data.update {
            it?.copy(player = it.player.copy(location = value))
        }
    }

    fun requirePlayer() = data.value!!.player

    val player: Player? = data.value?.player

    fun requireAccessToken() = data.value!!.accessToken

    fun requireRefreshToken() = data.value!!.refreshToken

    fun asStateFlow() = data.asStateFlow()
}
