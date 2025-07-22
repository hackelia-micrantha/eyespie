package com.micrantha.eyespie.core.data.account.model

import com.micrantha.eyespie.domain.entities.Location
import com.micrantha.eyespie.domain.entities.Player
import com.micrantha.eyespie.domain.entities.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.ExperimentalTime

object CurrentSession {
    private val data = MutableStateFlow<Session?>(null)

    fun update(value: Session) {
        data.update { value }
    }

    @OptIn(ExperimentalTime::class)
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
