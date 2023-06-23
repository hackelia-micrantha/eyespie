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
import com.micrantha.skouter.ui.component.combine
import com.micrantha.skouter.ui.scan.edit.ScanEditScreen
import com.micrantha.skouter.ui.scan.preview.ScanAction.EditSaved
import com.micrantha.skouter.ui.scan.preview.ScanAction.EditScan
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageCaptured
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageSaved
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveError
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveScan
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScanSavable
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedColors
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedLabels
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedMatch
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedObjects
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedSegments
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
        }
    }

    override fun reduce(state: ScanState, action: Action) = when (action) {
        is ImageCaptured -> state.copy(
            image = action.image,
        )

        is ScannedLabels -> state.copy(
            labels = state.labels.combine(action.labels)
        )

        is ScannedColors -> state.copy(
            colors = state.colors.combine(action.colors)
        )

        is ScannedObjects -> state.copy(
            detection = action.detections.firstOrNull(),
        )

        is ScannedSegments -> state.copy(
            segment = action.segments.firstOrNull()
        )

        is ScannedMatch -> state.copy(
            match = action.matches.firstOrNull()
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
