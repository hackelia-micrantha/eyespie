package com.micrantha.eyespie.features.scan.ui.capture

import com.micrantha.bluebell.app.Log
import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.arch.Effect
import com.micrantha.bluebell.arch.Reducer
import com.micrantha.bluebell.domain.repository.LocalizedRepository
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.Router.Options.Replace
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.eyespie.core.data.account.model.CurrentSession
import com.micrantha.eyespie.core.ui.component.combine
import com.micrantha.eyespie.domain.entities.ColorClue
import com.micrantha.eyespie.domain.entities.DetectClue
import com.micrantha.eyespie.domain.entities.LabelClue
import com.micrantha.eyespie.domain.entities.MatchClue
import com.micrantha.eyespie.domain.entities.SegmentClue
import com.micrantha.eyespie.domain.repository.LocationRepository
import com.micrantha.eyespie.features.scan.ui.capture.ScanAction.EditSaved
import com.micrantha.eyespie.features.scan.ui.capture.ScanAction.EditScan
import com.micrantha.eyespie.features.scan.ui.capture.ScanAction.ImageSaved
import com.micrantha.eyespie.features.scan.ui.capture.ScanAction.SaveError
import com.micrantha.eyespie.features.scan.ui.capture.ScanAction.SaveScan
import com.micrantha.eyespie.features.scan.ui.capture.ScanAction.ScanSavable
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.features.scan.ui.edit.ScanEditScreen
import com.micrantha.eyespie.features.scan.ui.usecase.AnalyzeCaptureUseCase
import com.micrantha.eyespie.features.scan.ui.usecase.SaveCaptureUseCase
import com.micrantha.eyespie.features.scan.ui.usecase.TakeCaptureUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ScanCaptureEnvironment(
    private val context: ScreenContext,
    private val takeCaptureUseCase: TakeCaptureUseCase,
    private val saveCaptureUseCase: SaveCaptureUseCase,
    private val analyzeCaptureUseCase: AnalyzeCaptureUseCase,
    private val currentSession: CurrentSession,
    private val locationRepository: LocationRepository,
    private val mapper: ScanCaptureStateMapper,
) : Reducer<ScanState>, Effect<ScanState>,
    Router by context.router,
    FileSystem by context.fileSystem,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n {

    init {
        analyzeCaptureUseCase.clues.onEach(::dispatch).launchIn(dispatchScope)
    }

    override suspend fun invoke(action: Action, state: ScanState) {
        when (action) {
            is EditScan -> takeCaptureUseCase(
                state.image!!
            ).onSuccess { url ->
                Log.d("image url: $url")
                dispatch(EditSaved(url))
            }.onFailure {
                Log.d("unable to save scan", it)
                dispatch(SaveError)
            }

            is EditSaved -> navigate(
                ScanEditScreen(
                    context = context,
                    proof = mapper.prove(state)
                ), options = Replace
            )

            is ImageSaved -> saveCaptureUseCase(mapper.prove(state))
                .onFailure {
                    Log.e("unable to save scan", it)
                    dispatch(SaveError)
                }
                .onSuccess {
                    navigateBack()
                }

            is SaveScan -> takeCaptureUseCase(
                state.image!!
            ).onSuccess { url ->
                dispatch(ImageSaved(url))
            }.onFailure {
                dispatch(SaveError)
            }

            is CameraImage -> analyzeCaptureUseCase(action).launchIn(dispatchScope)

            is ScanAction.Back -> context.router.navigateBack()
        }
    }

    override fun reduce(state: ScanState, action: Action) = when (action) {
        is CameraImage -> state.copy(image = action)

        is LabelClue -> state.copy(
            labels = state.labels.combine(action)
        )

        is ColorClue -> state.copy(
            colors = state.colors.combine(action)
        )

        is DetectClue -> state.copy(
            detection = action,
            labels = state.labels.combine(action.labels)
        )

        is SegmentClue -> state.copy(
            segment = action
        )

        is MatchClue -> state.copy(
            match = action
        )

        is ScanSavable -> state.copy(
            path = action.path,
        )

        is SaveScan, is EditScan -> state.copy(
            busy = true,
            enabled = false,
            playerID = currentSession.requirePlayer().id,
            location = locationRepository.currentLocation()
                ?: currentSession.requirePlayer().location,
        )

        is SaveError -> state.copy(
            enabled = true,
            busy = false
        )

        else -> state
    }
}
