package com.micrantha.skouter.ui.scan.edit

import com.benasher44.uuid.uuid4
import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.MappedScreenEnvironment
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.skouter.ui.component.Choice
import com.micrantha.skouter.ui.component.updateKey
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.ColorChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.CustomLabelChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.Init
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.LabelChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.LoadedImage
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.NameChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.SaveScanEdit
import com.micrantha.skouter.ui.scan.usecase.LoadCameraImageUseCase
import com.micrantha.skouter.ui.scan.usecase.SaveThingImageUseCase

class ScanEditEnvironment(
    private val context: ScreenContext,
    private val saveThingImageUseCase: SaveThingImageUseCase,
    private val loadCameraImageUseCase: LoadCameraImageUseCase,
) : MappedScreenEnvironment<ScanEditState, ScanEditUiState>,
    Dispatcher by context.dispatcher,
    Router by context.router {
    override fun reduce(state: ScanEditState, action: Action) = when (action) {
        is Init -> state.copy(
            labels = action.arg.proof.labels?.associate {
                uuid4().toString() to it
            }?.toMutableMap(),
            colors = action.arg.proof.colors?.associate {
                uuid4().toString() to it
            }?.toMutableMap(),
            path = action.arg.path
        )
        is LabelChanged -> state.copy(
            customLabel = null,
            labels = state.labels?.updateKey(action.data.key) { clue ->
                clue.copy(data = action.data.tag)
            }
        )
        is ColorChanged -> state.copy(
            colors = state.colors?.updateKey(action.data.key) { clue ->
                clue.copy(data = action.data.tag)
            }
        )
        is NameChanged -> state.copy(
            name = action.data
        )
        is LoadedImage -> state.copy(
            image = action.data
        )
        is CustomLabelChanged -> state.copy(customLabel = action.data)
        else -> state
    }

    override suspend fun invoke(action: Action, state: ScanEditState) {
        when (action) {
            is SaveScanEdit -> saveThingImageUseCase(
                state.asNewThing()
            ).onSuccess {
                navigateBack()
            }.onFailure {
                Log.e("saving scan", it)
            }
            is Init -> loadCameraImageUseCase(action.arg.path)
                .onSuccess {
                    dispatch(LoadedImage(it))
                }.onFailure {
                    Log.e("unable to load image", it)
                }
        }
    }

    override fun map(state: ScanEditState) = ScanEditUiState(
        labels = state.labels?.map {
            Choice(
                label = it.value.display(),
                tag = it.value.data,
                key = it.key
            )
        } ?: emptyList(),
        customLabel = state.customLabel,
        colors = state.colors?.map {
            Choice(
                label = it.value.display(),
                tag = it.value.data,
                key = it.key
            )
        } ?: emptyList(),
        name = state.name ?: "",
        image = state.image
    )
}
