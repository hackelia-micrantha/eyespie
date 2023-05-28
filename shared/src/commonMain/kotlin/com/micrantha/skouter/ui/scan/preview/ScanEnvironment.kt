package com.micrantha.skouter.ui.scan.preview

import com.micrantha.bluebell.FileSystem
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.StateMapper
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.domain.model.candidate
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageCaptured
import com.micrantha.skouter.ui.scan.preview.ScanAction.LabelScanned
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveScan
import com.micrantha.skouter.ui.scan.usecase.SaveThingImageUseCase
import io.github.aakira.napier.Napier

class ScanEnvironment(
    context: ScreenContext,
    private val saveThingImageUseCase: SaveThingImageUseCase
) : Reducer<ScanState>, Effect<ScanState>, StateMapper<ScanState, ScanUiState>,
    Router by context.router,
    FileSystem by context.fileSystem,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n {

    override suspend fun invoke(action: Action, state: ScanState) {
        when (action) {

            is SaveScan -> saveThingImageUseCase(state.image!!).onSuccess {
                Napier.d("image url: $it")
                navigateBack()
            }
            is LabelScanned -> Napier.d("scanned label ${action.data}")
        }
    }

    override fun reduce(state: ScanState, action: Action) = when (action) {
        is ImageCaptured -> state.copy(image = action.image)
        is LabelScanned -> state.copy(labels = action.data)
        else -> state
    }

    override fun map(state: ScanState) = ScanUiState(
        clues = mutableListOf<Clue<*>>().apply {
            state.labels?.candidate()?.let { add(it) }
            state.location?.let { add(it) }
        }
    )
}
