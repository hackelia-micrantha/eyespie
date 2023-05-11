package com.micrantha.skouter.ui.games.create

import com.micrantha.bluebell.domain.arch.Action

sealed class GameCreateAction : Action {
    object Save : GameCreateAction()

    data class NameChanged(val name: String) : GameCreateAction()

    object NameDone : GameCreateAction()
}
