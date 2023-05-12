package com.micrantha.skouter.ui.games.details

import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Thing.Image

data class GameDetailScreenArg(
    val id: String,
    val title: String
)

data class GameDetailsState(
    val status: UiResult<Game> = UiResult.Default,
    val game: Game? = null,
    val images: Map<String, UiResult<Painter>> = mapOf()
)

data class GameDetailsUiState(
    val status: UiResult<Game>,
    private val images: Map<String, UiResult<Painter>>
) {
    fun image(id: String): UiResult<Painter> = images[id] ?: UiResult.Default
}

sealed class GameDetailsAction : Action {
    data class Load(val id: String) : GameDetailsAction()
    data class Loaded(val game: Game) : GameDetailsAction()
    data class Error(val error: Throwable) : GameDetailsAction()
    data class LoadImage(val thingId: String, val image: Image) : GameDetailsAction()
    data class LoadedImage(val thingId: String, val data: Painter) :
        GameDetailsAction()

    data class ImageFailed(val thingId: String, val err: Throwable) : GameDetailsAction()
}
