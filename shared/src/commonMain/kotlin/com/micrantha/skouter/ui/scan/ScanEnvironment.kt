package com.micrantha.skouter.ui.scan

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.ext.failure
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.map
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.StateMapper
import com.micrantha.skouter.ui.components.S
import com.micrantha.skouter.ui.scan.ScanAction.ImageCaptured
import com.micrantha.skouter.ui.scan.ScanAction.Init
import com.micrantha.skouter.ui.scan.ScanAction.NoCamera
import com.micrantha.skouter.ui.scan.ScanAction.ScanError
import com.micrantha.skouter.ui.scan.ScanAction.ScannedClues
import com.micrantha.skouter.ui.scan.usecase.CameraCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.ScanImageUseCase

class ScanEnvironment(
    context: ScreenContext,
    private val cameraCaptureUseCase: CameraCaptureUseCase,
    private val scanImageUseCase: ScanImageUseCase
) : Reducer<ScanState>, Effect<ScanState>, StateMapper<ScanState, ScanUiState>,
    Router by context.router,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n {

    override suspend fun invoke(action: Action, state: ScanState) {
        when (action) {
            is Init -> cameraCaptureUseCase()
                .onFailure {
                    dispatch(NoCamera(it))
                }.onSuccess {
                    dispatch(ImageCaptured(it))
                }
            is ImageCaptured -> scanImageUseCase(action.data)
                .onFailure { dispatch(ScanError(it)) }
                .onSuccess { dispatch(ScannedClues(it)) }
        }
    }

    override fun reduce(state: ScanState, action: Action) = when (action) {
        is ImageCaptured -> state.copy(image = action.data)
        is ScannedClues -> state.copy(clues = action.data)
        is NoCamera -> state.copy(status = failure(S.NoCamera))
        is ScanError -> state.copy(status = failure(S.NoClues))
        else -> state
    }

    override fun map(state: ScanState) = ScanUiState(
        status = state.status.map {
            ScanUiState.Data(
                state.clues!!, state.image!!
            )
        }
    )
}
