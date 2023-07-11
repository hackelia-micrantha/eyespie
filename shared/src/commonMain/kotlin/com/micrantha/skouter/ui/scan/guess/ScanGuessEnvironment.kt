package com.micrantha.skouter.ui.scan.guess

import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.model.UiMessage
import com.micrantha.bluebell.domain.model.popup
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenEnvironment
import com.micrantha.skouter.domain.repository.ThingRepository
import com.micrantha.skouter.ui.component.S
import com.micrantha.skouter.ui.scan.guess.ScanGuessAction.ImageCaptured
import com.micrantha.skouter.ui.scan.guess.ScanGuessAction.Load
import com.micrantha.skouter.ui.scan.guess.ScanGuessAction.Loaded
import com.micrantha.skouter.ui.scan.guess.ScanGuessAction.ThingMatched
import com.micrantha.skouter.ui.scan.guess.ScanGuessAction.ThingNotFound
import com.micrantha.skouter.ui.scan.usecase.MatchCameraImageUseCase

class ScanGuessEnvironment(
    private val args: ScanGuessArgs,
    private val context: ScreenContext,
    private val matchCameraImageUseCase: MatchCameraImageUseCase,
    private val thingRepository: ThingRepository
) : ScreenEnvironment<ScanGuessState>,
    Dispatcher by context.dispatcher,
    Router by context.router {

    override fun reduce(state: ScanGuessState, action: Action) = when (action) {
        is Loaded -> state.copy(thing = action.thing)
        else -> state
    }

    override suspend fun invoke(action: Action, state: ScanGuessState) {
        when (action) {
            is Load -> thingRepository.thing(args.id).onSuccess {
                dispatch(Loaded(it))
            }.onFailure {
                dispatch(context.popup(S.NoDataFound) {
                    navigateBack()
                })
            }

            is ImageCaptured -> if (state.thing != null) {
                matchCameraImageUseCase(
                    action.image,
                    state.thing.embedding
                ).onSuccess { matched ->
                    if (matched) {
                        dispatch(ThingMatched)
                        navigateBack()
                    } else {
                        dispatch(ThingNotFound)
                        // keep trying!
                        // TODO: display warmer/colder
                    }
                }.onFailure {
                    Log.e("unable to match", it)
                }
            }

            is UiMessage -> Log.d(action.message)

            is ThingMatched -> Log.d("Thing found") // Show animation
            is ThingNotFound -> Log.d("Thing not found") // Launch listing view
        }
    }
}
