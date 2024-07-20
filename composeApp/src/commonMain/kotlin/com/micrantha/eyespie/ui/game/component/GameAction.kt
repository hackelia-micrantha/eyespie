package com.micrantha.eyespie.ui.game.component

import com.micrantha.eyespie.domain.model.Game
import com.micrantha.eyespie.ui.game.detail.GameDetailScreenArg

sealed class GameAction {

    data object Load : GameAction()

    data class Failure(val error: Throwable) : GameAction()

    data class LoadedImage(val id: String, val data: String) :
        GameAction()

    data class ImageFailed(val id: String, val err: Throwable) : GameAction()

    data class GameClicked(
        val game: Game.Listing,
        val arg: GameDetailScreenArg = GameDetailScreenArg(game.nodeId, game.name)
    ) : GameAction()
}