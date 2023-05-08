package com.micrantha.skouter.ui.games.details

import androidx.compose.ui.graphics.ImageBitmap
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.ResultStatus
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Thing.Image
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.domain.repository.ThingsRepository

data class GameDetailsState(
    val status: ResultStatus<Game> = ResultStatus.Default,
    val game: Game? = null
)

data class GameDetailsUiState(
    val status: ResultStatus<Game>
)

fun GameDetailsState.asUiState() = GameDetailsUiState(
    status = this.status
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

fun Game.updateThingImage(thingId: String, data: ImageBitmap): Game = this.copy(
    things = this.things.map { thing ->
        if (thing.id == thingId) {
            thing.copy(image = thing.image?.copy(data = data))
        } else {
            thing
        }
    }
)
