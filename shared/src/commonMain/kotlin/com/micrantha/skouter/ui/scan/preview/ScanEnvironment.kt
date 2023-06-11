package com.micrantha.skouter.ui.scan.preview

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
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
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.domain.model.DetectClue.Box
import com.micrantha.skouter.domain.model.LocationClue
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.ui.scan.edit.ScanEditArg
import com.micrantha.skouter.ui.scan.edit.ScanEditScreen
import com.micrantha.skouter.ui.scan.preview.ScanAction.ColorScanned
import com.micrantha.skouter.ui.scan.preview.ScanAction.EditScan
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageCaptured
import com.micrantha.skouter.ui.scan.preview.ScanAction.LabelScanned
import com.micrantha.skouter.ui.scan.preview.ScanAction.ObjectScanned
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveError
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveScan
import com.micrantha.skouter.ui.scan.usecase.CameraCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.SaveThingImageUseCase
import okio.Path

class ScanEnvironment(
    private val context: ScreenContext,
    private val cameraCaptureUseCase: CameraCaptureUseCase,
    private val saveThingImageUseCase: SaveThingImageUseCase,
    private val currentSession: CurrentSession
) : Reducer<ScanState>, Effect<ScanState>, StateMapper<ScanState, ScanUiState>,
    Router by context.router,
    FileSystem by context.fileSystem,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n {

    override suspend fun invoke(action: Action, state: ScanState) {
        when (action) {
            is EditScan -> cameraCaptureUseCase(
                state.image!!.toByteArray()
            ).onSuccess { url ->
                Log.d("image url: $url")
                navigate(editScreen(state, url), options = Replace)
            }.onFailure {
                Log.d("unable to save scan", it)
                dispatch(SaveError)
            }
            is SaveScan -> cameraCaptureUseCase(
                state.image!!.toByteArray()
            ).onSuccess { url ->
                saveThingImageUseCase(newThing(state, url))
                    .onFailure {
                        Log.e("unable to save scan", it)
                    }
                    .onSuccess {
                        navigateBack()
                    }
            }
            is LabelScanned -> Log.d("scanned label ${action.data}")
            is ColorScanned -> Log.d("scanned color ${action.data}")
            is ObjectScanned -> Log.d("scanned object ${action.data}")
        }
    }

    override fun reduce(state: ScanState, action: Action) = when (action) {
        is ImageCaptured -> state.copy(
            image = action.image,
        )
        is LabelScanned -> state.copy(
            labels = state.labels.combine(action.data),
        )
        is ColorScanned -> state.copy(
            colors = state.colors.combine(action.data),
        )
        is ObjectScanned -> state.copy(
            objects = state.objects.combine(action.data),
            current = action.data.firstOrNull()
        )
        is SaveScan, is EditScan -> state.copy(
            enabled = false,
            location = currentSession.player?.location?.let { LocationClue(it) }
        )
        else -> state
    }

    override fun map(state: ScanState) = ScanUiState(
        clues = state.clues(),
        current = state.current?.let {
            Pair(
                it.data.asComposeRect(),
                it.labels.firstOrNull()?.display() ?: ""
            )
        },
        imageSize = state.image?.asSize() ?: Size(0f, 0f),
        enabled = state.enabled
    )

    private fun editScreen(state: ScanState, path: Path) = ScanEditScreen(
        context = context,
        arg = ScanEditArg(
            state.asProof(),
            path,
        )
    )

    private fun newThing(state: ScanState, path: Path) = Thing.Create(
        proof = state.asProof(),
        name = "Something that is ${state.colors?.first()}",
        path = path
    )

    private fun ScanState.clues() = mutableSetOf<Clue<*>>().apply {
        labels?.let { addAll(it.take(3)) }
        colors?.let { addAll(it.take(3)) }
        location?.let { add(it) }
        objects?.take(3)?.forEach { addAll(it.labels.take(3)) }
    }

    private fun <T> Set<T>?.combine(other: Set<T>): Set<T> {
        if (this == null) return other
        return this.plus(other)
    }

    private fun Box.asComposeRect() = Rect(
        this.x, this.y, this.w, this.h
    )

    private fun CameraImage.asSize() = Size(width.toFloat(), height.toFloat())
}
