package com.micrantha.skouter.ui.games.details

import androidx.compose.ui.graphics.ImageBitmap
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.ResultStatus
import com.micrantha.bluebell.domain.model.busy
import com.micrantha.bluebell.domain.model.copy
import com.micrantha.bluebell.domain.model.error
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Thing.Image
import com.micrantha.skouter.ui.MainAction
import com.micrantha.skouter.ui.arch.i18n.LoadingGame
import com.micrantha.skouter.ui.arch.toi18n
import com.micrantha.skouter.ui.games.details.GameDetailsAction.Error
import com.micrantha.skouter.ui.games.details.GameDetailsAction.Load
import com.micrantha.skouter.ui.games.details.GameDetailsAction.LoadImage
import com.micrantha.skouter.ui.games.details.GameDetailsAction.Loaded
import com.micrantha.skouter.ui.games.details.GameDetailsAction.LoadedImage

sealed class GameDetailsAction : Action {
    data class Load(val id: String) : GameDetailsAction()
    data class Loaded(val game: Game) : GameDetailsAction()
    data class Error(val error: Throwable) : GameDetailsAction()
    data class LoadImage(val thingId: String, val image: Image) : GameDetailsAction()
    data class LoadedImage(val thingId: String, val data: ImageBitmap) :
        GameDetailsAction()
}

class GameDetailsReducer(
    private val strings: LocalizedRepository
) : Reducer<GameDetailsState> {
    override fun invoke(state: GameDetailsState, action: Action) = when (action) {
        is Load -> state.copy(status = strings.busy(LoadingGame))
        is Loaded -> state.copy(status = ResultStatus.Ready(action.game))
        is Error -> state.copy(status = strings.error(action.error.toi18n()))
        is LoadedImage -> state.copy(
            status = state.status.copy(ready = { game ->
                game.updateThingImage(action.thingId, action.data)
            })
        )
        else -> state
    }
}

class GameDetailsEffects(
    private val environment: GameDetailsEnvironment
) : Effect<GameDetailsState>, LocalizedRepository by environment, Dispatcher by environment {
    override suspend fun invoke(action: Action, state: GameDetailsState) {
        when (action) {
            is Load -> environment.game(action.id).onFailure {
                dispatch(Error(it))
            }.onSuccess {
                dispatch(Loaded(it))
            }
            is Loaded -> dispatch(
                MainAction.SetTitle(action.game.name)
            )
            is LoadImage -> environment.image(action.image)
                .onFailure {
                    dispatch(Error(it))
                }.onSuccess {
                    dispatch(LoadedImage(action.thingId, it))
                }
        }
    }
}
