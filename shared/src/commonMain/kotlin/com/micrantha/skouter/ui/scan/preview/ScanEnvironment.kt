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
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.ui.scan.edit.ScanEditArg
import com.micrantha.skouter.ui.scan.edit.ScanEditScreen
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageCaptured
import com.micrantha.skouter.ui.scan.preview.ScanAction.LabelScanned
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveError
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveScan
import com.micrantha.skouter.ui.scan.usecase.CameraCaptureUseCase

class ScanEnvironment(
    private val context: ScreenContext,
    private val cameraCaptureUseCase: CameraCaptureUseCase,
) : Reducer<ScanState>, Effect<ScanState>, StateMapper<ScanState, ScanUiState>,
    Router by context.router,
    FileSystem by context.fileSystem,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n {

    override suspend fun invoke(action: Action, state: ScanState) {
        when (action) {
            is SaveScan -> cameraCaptureUseCase(
                state.image!!.toByteArray()
            ).onSuccess {
                Log.d("image url: $it")
                navigate(
                    ScanEditScreen(
                        ScanEditArg(
                            proof = state.asProof(),
                            image = state.image,
                        ), context
                    ), options = Replace
                )
            }.onFailure {
                Log.d("unable to save scan", it)
                dispatch(SaveError)
            }
            is LabelScanned -> Log.d("scanned label ${action.data}")
        }
    }

    override fun reduce(state: ScanState, action: Action) = when (action) {
        is ImageCaptured -> state.copy(image = action.image)
        is LabelScanned -> state.copy(labels = action.data)
        is SaveScan -> state.copy(enabled = false)
        else -> state
    }

    override fun map(state: ScanState) = ScanUiState(
        clues = state.clues(),
        enabled = state.enabled
    )

    fun ScanState.clues() = mutableListOf<Clue<*>>().apply {
        labels?.minOrNull()?.let { add(it) }
    }
}
