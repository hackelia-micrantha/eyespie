package com.micrantha.skouter.ui

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.models.Player

sealed class MainAction : Action {

    data class Loaded(val player: Player) : MainAction()

    object Login : MainAction()

    object Load : MainAction()

}
