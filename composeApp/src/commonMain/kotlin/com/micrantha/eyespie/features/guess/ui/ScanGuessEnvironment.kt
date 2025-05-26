package com.micrantha.eyespie.features.guess.ui

import com.micrantha.bluebell.app.Log
import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.message.popup
import com.micrantha.bluebell.ui.model.UiMessage
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenEnvironment
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.domain.repository.ThingRepository
import com.micrantha.eyespie.features.guess.ui.ScanGuessAction.ImageCaptured
import com.micrantha.eyespie.features.guess.ui.ScanGuessAction.Load
import com.micrantha.eyespie.features.guess.ui.ScanGuessAction.Loaded
import com.micrantha.eyespie.features.guess.ui.ScanGuessAction.ThingMatched
import com.micrantha.eyespie.features.guess.ui.ScanGuessAction.ThingNotFound
import com.micrantha.eyespie.features.scan.ui.usecase.MatchCaptureUseCase
import eyespie.composeapp.generated.resources.no_data_found
import org.jetbrains.compose.resources.getString

class ScanGuessEnvironment(
    private val args: ScanGuessArgs,
    private val context: ScreenContext,
    private val matchCaptureUseCase: MatchCaptureUseCase,
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
                dispatch(context.popup(S.no_data_found) {
                    navigateBack()
                })
            }

            is ImageCaptured -> if (state.thing != null) {
                matchCaptureUseCase(
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

            is UiMessage -> Log.d(getString(action.message))

            is ThingMatched -> Log.d("Thing found") // Show animation
            is ThingNotFound -> Log.d("Thing not found") // Launch listing view
        }
    }
}
