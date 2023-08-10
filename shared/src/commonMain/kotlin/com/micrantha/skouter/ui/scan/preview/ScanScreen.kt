package com.micrantha.skouter.ui.scan.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.arch.StoreDispatch
import com.micrantha.bluebell.ui.components.status.LoadingContent
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.platform.scan.CameraScanner
import com.micrantha.skouter.ui.component.LocationEnabledEffect
import com.micrantha.skouter.ui.scan.components.ScannedClues
import com.micrantha.skouter.ui.scan.components.ScannedOverlays
import com.micrantha.skouter.ui.scan.preview.ScanAction.EditScan
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveScan
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedImage
import dev.icerock.moko.permissions.Permission.CAMERA
import dev.icerock.moko.permissions.PermissionsController
import org.kodein.di.compose.rememberInstance

class ScanScreen : Screen {
    @Composable
    override fun Content() {
        val permissions by rememberInstance<PermissionsController>()

        LaunchedEffect(Unit) {
            permissions.providePermission(CAMERA)
        }

        LocationEnabledEffect()

        val viewModel: ScanScreenModel = rememberScreenModel()

        val state by viewModel.state.collectAsState()

        Render(state, viewModel::invoke, viewModel::dispatch)
    }

    @Composable
    private fun Render(state: ScanUiState, onScan: StoreDispatch, dispatch: Dispatch) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.capture != null -> renderCapture(state.capture)
                state.enabled.not() -> LoadingContent()
                else -> renderCamera(state, dispatch, onScan)
            }
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.renderCamera(
    state: ScanUiState,
    dispatch: Dispatch,
    onScan: StoreDispatch
) {
    TextButton(
        modifier = Modifier.align(Alignment.TopStart).size(Dimensions.touchable)
            .padding(top = Dimensions.screen, start = Dimensions.screen),
        onClick = {
            dispatch(ScanAction.Back)
        }) {
        Icon(
            imageVector = Icons.Default.ChevronLeft,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface
        )
    }
    CameraScanner(
        modifier = Modifier.align(Alignment.TopCenter).fillMaxSize(),
    ) {
        onScan(ScannedImage(it))
    }

    if (state.overlays.isNotEmpty()) {
        ScannedOverlays(
            data = state.overlays
        )
    }

    if (state.clues.isNotEmpty()) {
        ScannedClues(
            clues = state.clues,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }

    Row(
        modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
            .padding(Dimensions.content),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            enabled = state.enabled,
            onClick = { dispatch(EditScan) }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        }

        IconButton(
            enabled = state.enabled,
            onClick = { dispatch(SaveScan) }
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.renderCapture(capture: Painter) {
    Image(
        modifier = Modifier.align(Alignment.TopCenter).fillMaxSize(),
        painter = capture,
        contentDescription = null
    )
}
