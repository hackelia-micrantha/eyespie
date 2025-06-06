package com.micrantha.eyespie.features.game.ui.create

data class GameCreateState(
    val name: String = "",
    val expires: String? = null,
    val turnDuration: String? = null,
    val playerLimits: IntRange? = null,
    val thingLimits: IntRange? = null
)

sealed interface GameCreateAction {
    data object Save : GameCreateAction

    data class NameChanged(val name: String) : GameCreateAction

    data object NameDone : GameCreateAction
}
