package com.micrantha.skouter.ui.game.details

import com.micrantha.bluebell.data.err.fail
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.ext.busy
import com.micrantha.bluebell.domain.ext.failure
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.Ready
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.bluebell.domain.model.mapNotNull
import com.micrantha.bluebell.ui.toPainter
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.domain.repository.StorageRepository
import com.micrantha.skouter.ui.components.Strings.LoadingGame
import com.micrantha.skouter.ui.components.toi18n
import com.micrantha.skouter.ui.game.action.GameAction.Failure
import com.micrantha.skouter.ui.game.action.GameAction.ImageFailed
import com.micrantha.skouter.ui.game.action.GameAction.LoadImage
import com.micrantha.skouter.ui.game.action.GameAction.LoadedImage
import com.micrantha.skouter.ui.game.details.GameDetailsAction.Load
import com.micrantha.skouter.ui.game.details.GameDetailsAction.Loaded
import io.github.aakira.napier.Napier

class GameDetailsEnvironment(
    private val dispatcher: Dispatcher,
    private val localizedRepository: LocalizedRepository,
    private val gameRepository: GameRepository,
    private val storageRepository: StorageRepository,
) : Reducer<GameDetailsState>, Effect<GameDetailsState>, Dispatcher by dispatcher,
    LocalizedRepository by localizedRepository {

    fun map(state: GameDetailsState) = GameDetailsUiState(
        status = state.status.mapNotNull { state.game }
    )

    override fun reduce(state: GameDetailsState, action: Action) = when (action) {
        is Load -> state.copy(status = busy(LoadingGame))
        is Loaded -> state.copy(
            status = Ready(),
            game = action.game,
            images = action.images.toMutableMap()
        )
        is Failure -> state.copy(status = failure(action.error.toi18n()))
        is LoadedImage -> state.apply {
            val ref = images[action.id] ?: fail("no image found for ${action.id}")
            images[action.id] = ref.copy(status = Ready(action.data))
        }
        is LoadImage -> state.copy()
        is ImageFailed -> state.copy()
        else -> state
    }

    override suspend fun invoke(action: Action, state: GameDetailsState) {
        when (action) {
            is Load -> {
                gameRepository.game(action.arg.id).onFailure {
                    dispatch(Failure(it))
                }.onSuccess { game ->
                    dispatch(Loaded(game))
                }
            }
            is LoadImage -> storageRepository.download(action.image)
                .onFailure {
                    Napier.e("load image failed - ${action.image.path}", it)
                    dispatch(ImageFailed(action.image.id, it))
                }.onSuccess {
                    dispatch(
                        LoadedImage(action.image.id, it.toPainter())
                    )
                }
        }
    }
}
