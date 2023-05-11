package com.micrantha.skouter.ui.games.details

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.UiResult.Busy
import com.micrantha.bluebell.domain.model.UiResult.Failure
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.bluebell.domain.model.busy
import com.micrantha.bluebell.domain.model.error
import com.micrantha.bluebell.ui.scaffold.ScaffoldAction
import com.micrantha.bluebell.ui.toPainter
import com.micrantha.skouter.data.things.model.path
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.domain.repository.ThingsRepository
import com.micrantha.skouter.ui.components.i18n.LoadingGame
import com.micrantha.skouter.ui.components.toi18n
import com.micrantha.skouter.ui.games.details.GameDetailsAction.Error
import com.micrantha.skouter.ui.games.details.GameDetailsAction.ImageFailed
import com.micrantha.skouter.ui.games.details.GameDetailsAction.Load
import com.micrantha.skouter.ui.games.details.GameDetailsAction.LoadImage
import com.micrantha.skouter.ui.games.details.GameDetailsAction.Loaded
import com.micrantha.skouter.ui.games.details.GameDetailsAction.LoadedImage
import io.github.aakira.napier.Napier

class GameDetailsEnvironment(
    private val dispatcher: Dispatcher,
    private val localizedRepository: LocalizedRepository,
    private val gameRepository: GameRepository,
    private val thingsRepository: ThingsRepository,
) : Reducer<GameDetailsState>, Effect<GameDetailsState>, Dispatcher by dispatcher,
    LocalizedRepository by localizedRepository {

    override fun reduce(state: GameDetailsState, action: Action) = when (action) {
        is Load -> state.copy(status = busy(LoadingGame))
        is Loaded -> state.copy(status = Ready(action.game))
        is Error -> state.copy(status = error(action.error.toi18n()))
        is LoadedImage -> state.copy(
            images = state.images.plus(
                action.thingId to Ready(action.data)
            )
        )
        is LoadImage -> state.copy(
            images = state.images.plus(
                action.thingId to Busy()
            )
        )
        is ImageFailed -> state.copy(
            images = state.images.plus(
                action.thingId to Failure()
            )
        )
        else -> state
    }

    override suspend fun invoke(action: Action, state: GameDetailsState) {
        when (action) {
            is Load -> gameRepository.game(action.id).onFailure {
                dispatch(Error(it))
            }.onSuccess {
                dispatch(Loaded(it))
            }
            is Loaded -> dispatch(
                ScaffoldAction.SetTitle(action.game.name)
            )
            is LoadImage -> thingsRepository.image(action.image)
                .onFailure {
                    Napier.e("load image ${action.image.path}", it)
                    dispatch(ImageFailed(action.thingId, it))
                }.onSuccess {
                    dispatch(LoadedImage(action.thingId, it.toPainter()))
                }
        }
    }
}
