package com.micrantha.skouter.ui.games.details

import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Thing.Image
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.domain.repository.ThingsRepository

typealias ImageEntry = Map.Entry<String, UiResult<Painter>>

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

fun GameDetailsState.asUiState() = GameDetailsUiState(
    status = this.status,
    images = this.images
)

class GameDetailsEnvironment(
    private val gameRepository: GameRepository,
    private val thingsRepository: ThingsRepository,
    private val dispatcher: Dispatcher,
    private val localizedRepository: LocalizedRepository,
) : Dispatcher by dispatcher, LocalizedRepository by localizedRepository {
    suspend fun game(id: String) = gameRepository.game(id)

    suspend fun image(image: Image) = thingsRepository.image(image)
}
