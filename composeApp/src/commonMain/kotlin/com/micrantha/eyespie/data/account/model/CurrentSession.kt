package com.micrantha.eyespie.data.account.model

import com.micrantha.eyespie.domain.model.Location
import com.micrantha.eyespie.domain.model.Player
import com.micrantha.eyespie.domain.model.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CurrentSession {
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
