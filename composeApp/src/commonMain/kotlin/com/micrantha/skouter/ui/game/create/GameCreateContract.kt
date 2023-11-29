package com.micrantha.skouter.ui.game.create

import com.micrantha.bluebell.domain.arch.Action

data class GameCreateState(
    val name: String = "",
    val expires: String? = null,
    val turnDuration: String? = null,
    val playerLimits: IntRange? = null,
    val thingLimits: IntRange? = null
)

sealed interface GameCreateAction : Action {
    data object Save : GameCreateAction

    data class NameChanged(val name: String) : GameCreateAction

    data object NameDone : GameCreateAction
}
