package com.micrantha.skouter.ui.scan.guess

import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.ui.screen.ScreenEnvironment
import com.micrantha.skouter.ui.scan.guess.ScanGuessAction.ImageCaptured
import com.micrantha.skouter.ui.scan.guess.ScanGuessAction.ThingMatched
import com.micrantha.skouter.ui.scan.guess.ScanGuessAction.ThingNotFound
import com.micrantha.skouter.ui.scan.usecase.MatchCameraImageUseCase

class ScanGuessEnvironment(
    private val args: ScanGuessArgs,
    private val dispatcher: Dispatcher,
    private val matchCameraImageUseCase: MatchCameraImageUseCase
) : ScreenEnvironment<ScanGuessState>, Dispatcher by dispatcher {

    override fun reduce(state: ScanGuessState, action: Action) = when (action) {
        else -> state
    }

    override suspend fun invoke(action: Action, state: ScanGuessState) {
        when (action) {
            is ImageCaptured -> matchCameraImageUseCase(action.image).onSuccess { matches ->
                if (matches.any { it.id == args.id }) {
                    dispatch(ThingMatched)
                } else {
                    dispatch(ThingNotFound)
                }
            }.onFailure {
                Log.e("unable to match", it)
            }

            is ThingMatched -> Log.d("Thing found") // Show animation
            is ThingNotFound -> Log.d("Thing not found") // Launch listing view
        }
    }
}
