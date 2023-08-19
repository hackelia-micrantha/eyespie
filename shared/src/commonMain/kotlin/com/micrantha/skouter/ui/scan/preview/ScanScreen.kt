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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.ui.components.StateRenderer
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

class ScanScreen : Screen, StateRenderer<ScanUiState> {
    @Composable
    override fun Content() {
        val permissions by rememberInstance<PermissionsController>()

        LaunchedEffect(Unit) {
            permissions.providePermission(CAMERA)
        }

        LocationEnabledEffect()

        val screenModel: ScanScreenModel = rememberScreenModel()

        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
    }

    @Composable
    override fun Render(state: ScanUiState, dispatch: Dispatch) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.enabled.not() -> LoadingContent()
                state.capture != null -> renderCapture(state, dispatch)
                else -> renderCamera(state, dispatch)
            }

        }
    }
}

@Composable
private fun BoxWithConstraintsScope.renderCamera(
    state: ScanUiState,
    dispatch: Dispatch
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
        dispatch.send(ScannedImage(it))
    }

    renderClues(state, dispatch)
}

@Composable
private fun BoxWithConstraintsScope.renderCapture(state: ScanUiState, dispatch: Dispatch) {
    Image(
        modifier = Modifier.align(Alignment.TopCenter).fillMaxSize(),
        painter = state.capture!!,
        contentDescription = null
    )
    renderClues(state, dispatch)
}

@Composable
private fun BoxWithConstraintsScope.renderClues(state: ScanUiState, dispatch: Dispatch) {

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
            modifier = Modifier.background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                RoundedCornerShape(Dimensions.content)
            ),
            enabled = state.enabled,
            onClick = { dispatch(EditScan) }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }

        IconButton(
            modifier = Modifier.background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                RoundedCornerShape(Dimensions.content)
            ),
            enabled = state.enabled,
            onClick = { dispatch(SaveScan) }
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
    }
}
