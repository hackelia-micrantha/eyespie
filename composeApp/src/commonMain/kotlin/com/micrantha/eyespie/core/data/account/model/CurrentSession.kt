package com.micrantha.eyespie.core.data.account.model

import com.micrantha.eyespie.domain.entities.Location
import com.micrantha.eyespie.features.players.domain.entities.Player
import com.micrantha.eyespie.domain.entities.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.ExperimentalTime

object CurrentSession {
    private val data = MutableStateFlow<Session?>(null)
    var player: Player? = null
       private set

    fun update(value: Session) {
        data.update { value }
    }

    fun update(value: Player) {
        player = value
    }

    @OptIn(ExperimentalTime::class)
    fun update(value: Location) {
        player = player?.copy(location = value)
    }

    val isValid = data.value != null

    fun requireAccessToken() = data.value!!.accessToken

    fun requireRefreshToken() = data.value!!.refreshToken

    fun requireUserId() = data.value!!.userId

    fun requirePlayer() = player!!

    fun asStateFlow() = data.asStateFlow()
}
