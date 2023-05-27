package com.micrantha.skouter.ui.scan

import com.micrantha.bluebell.FileSystem
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.ext.failure
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.Ready
import com.micrantha.bluebell.domain.model.map
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.StateMapper
import com.micrantha.bluebell.ui.toPainter
import com.micrantha.skouter.ui.components.S
import com.micrantha.skouter.ui.scan.ScanAction.ImageCaptured
import com.micrantha.skouter.ui.scan.ScanAction.Init
import com.micrantha.skouter.ui.scan.ScanAction.NameChanged
import com.micrantha.skouter.ui.scan.ScanAction.NoCamera
import com.micrantha.skouter.ui.scan.ScanAction.SaveScan
import com.micrantha.skouter.ui.scan.ScanAction.ScanError
import com.micrantha.skouter.ui.scan.ScanAction.ScannedClues
import com.micrantha.skouter.ui.scan.ScanAction.TestSave
import com.micrantha.skouter.ui.scan.usecase.CameraCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.SaveThingImageUseCase
import com.micrantha.skouter.ui.scan.usecase.ScanImageUseCase
import io.github.aakira.napier.Napier

class ScanEnvironment(
    context: ScreenContext,
    private val cameraCaptureUseCase: CameraCaptureUseCase,
    private val scanImageUseCase: ScanImageUseCase,
    private val saveThingImageUseCase: SaveThingImageUseCase
) : Reducer<ScanState>, Effect<ScanState>, StateMapper<ScanState, ScanUiState>,
    Router by context.router,
    FileSystem by context.fileSystem,
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
            is ImageCaptured -> scanImageUseCase(action.path)
                .onFailure { /*dispatch(ScanError(it))*/ dispatch(TestSave) }
                .onSuccess { dispatch(ScannedClues(it)) }
            is SaveScan -> saveThingImageUseCase(state.name, state.image!!).onSuccess {
                Napier.d("image url: $it")
                navigateBack()
            }
        }
    }

    override fun reduce(state: ScanState, action: Action) = when (action) {
        is ImageCaptured -> state.copy(image = action.path)
        is NameChanged -> state.copy(name = action.data)
        is ScannedClues -> state.copy(clues = action.data, status = Ready())
        is TestSave -> state.copy(status = Ready())
        is NoCamera -> state.copy(status = failure(S.NoCamera))
        is ScanError -> state.copy(status = failure(S.NoClues))
        else -> state
    }

    override fun map(state: ScanState) = ScanUiState(
        status = state.status.map {
            ScanUiState.Data(
                clues = state.clues,
                image = {
                    read(state.image!!).toPainter()
                },
                name = state.name
            )
        }
    )
}
