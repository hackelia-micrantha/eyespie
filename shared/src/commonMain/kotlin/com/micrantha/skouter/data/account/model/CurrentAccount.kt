package com.micrantha.skouter.data.account.model

import com.micrantha.skouter.domain.models.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CurrentAccount {
    private val player = MutableStateFlow<Player?>(null)

    fun update(value: Player) {
        player.update { value }
    }

    fun player() = player.value
    
    fun asFlow() = player.asStateFlow()
}
