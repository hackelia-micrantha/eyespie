package com.micrantha.skouter.ui.scan.preview

import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.Router.Options.Replace
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.StateMapper
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.platform.ImageCaptured
import com.micrantha.skouter.ui.component.combine
import com.micrantha.skouter.ui.scan.edit.ScanEditScreen
import com.micrantha.skouter.ui.scan.preview.ScanAction.EditSaved
import com.micrantha.skouter.ui.scan.preview.ScanAction.EditScan
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageSaved
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageScanned
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveError
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveScan
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScanSavable
import com.micrantha.skouter.ui.scan.usecase.AnalyzeCameraImageUseCase
import com.micrantha.skouter.ui.scan.usecase.CameraCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.SaveThingImageUseCase

class ScanEnvironment(
    private val context: ScreenContext,
    private val cameraCaptureUseCase: CameraCaptureUseCase,
    private val saveThingImageUseCase: SaveThingImageUseCase,
    private val analyzeCameraImageUseCase: AnalyzeCameraImageUseCase,
    private val currentSession: CurrentSession
) : Reducer<ScanState>, Effect<ScanState>, StateMapper<ScanState, ScanUiState>,
    Router by context.router,
    FileSystem by context.fileSystem,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n {

    override suspend fun invoke(action: Action, state: ScanState) {
        when (action) {
            is EditScan -> cameraCaptureUseCase(
                state.image!!
            ).onSuccess { url ->
                Log.d("image url: $url")
                dispatch(EditSaved(url))
            }.onFailure {
                Log.d("unable to save scan", it)
                dispatch(SaveError)
            }

            is EditSaved -> navigate(state.editScreen(), options = Replace)

            is ImageSaved -> saveThingImageUseCase(state.asProof())
                .onFailure {
                    Log.e("unable to save scan", it)
                    dispatch(SaveError)
                }
                .onSuccess {
                    navigateBack()
                }

            is SaveScan -> cameraCaptureUseCase(
                state.image!!
            ).onSuccess { url ->
                dispatch(ImageSaved(url))
            }.onFailure {
                dispatch(SaveError)
            }

            is ImageCaptured -> analyzeCameraImageUseCase(action.image)
                .collect { result ->
                    result
                        .onSuccess { dispatch(it) }
                        .onFailure { Log.e("unable to analyze image", it) }
                }
        }
    }

    override fun reduce(state: ScanState, action: Action) = when (action) {
        is ImageCaptured -> state.copy(
            image = action.image,
        )

        is ImageScanned -> state.copy(
            labels = state.labels.combine(action.label),
            colors = state.colors.combine(action.color),
            detection = action.detection,
            segment = action.segment,
            match = action.match.data
        )

        is ScanSavable -> state.copy(
            path = action.path,
            location = currentSession.player?.location,
            playerID = currentSession.player?.id
        )

        is SaveScan, is EditScan -> state.copy(
            enabled = false,
        )

        is SaveError -> state.copy(
            enabled = false
        )

        else -> state
    }

    override fun map(state: ScanState) = ScanUiState(
        clues = state.clues(),
        overlays = emptyList(),
        enabled = state.enabled
    )

    private fun ScanState.editScreen() = ScanEditScreen(
        context = context,
        proof = asProof()
    )
}
