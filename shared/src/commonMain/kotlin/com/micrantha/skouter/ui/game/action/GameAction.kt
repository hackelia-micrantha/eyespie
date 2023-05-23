package com.micrantha.skouter.ui.game.action

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Image
import com.micrantha.skouter.domain.models.ImageDownload
import com.micrantha.skouter.ui.game.details.GameDetailScreenArg

sealed class GameAction : Action {

    object Load : GameAction()

    data class Failure(val error: Throwable) : GameAction()
    data class LoadImage(val image: Image) : GameAction()
    data class LoadedImage(val id: String, val data: ImageDownload) :
        GameAction()

    data class ImageFailed(val id: String, val err: Throwable) : GameAction()

    data class GameClicked(
        val game: Game.Listing,
        val arg: GameDetailScreenArg = GameDetailScreenArg(game.nodeId, game.name)
    ) : GameAction()
}
