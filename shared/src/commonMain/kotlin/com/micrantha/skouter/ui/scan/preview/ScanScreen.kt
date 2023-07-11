package com.micrantha.skouter.ui.scan.preview

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.arch.StoreDispatch
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.platform.CameraScanner
import com.micrantha.skouter.ui.component.LocationEnabledEffect
import com.micrantha.skouter.ui.scan.components.ScannedClues
import com.micrantha.skouter.ui.scan.components.ScannedOverlays
import com.micrantha.skouter.ui.scan.preview.ScanAction.EditScan
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageCaptured
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveScan
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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CameraScanner(
                modifier = Modifier.align(Alignment.TopCenter).fillMaxSize(),
            ) {
                onScan(ImageCaptured(it))
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
                    .padding(Dimensions.content)
            ) {
                Button(
                    enabled = state.enabled,
                    modifier = Modifier.weight(0.5f),
                    onClick = { dispatch(EditScan) }
                ) {
                    Text("Edit")
                }

                Spacer(modifier = Modifier.width(Dimensions.content))

                Button(
                    enabled = state.enabled,
                    modifier = Modifier.weight(0.5f),
                    onClick = { dispatch(SaveScan) }
                ) {
                    Text("Save")
                }
            }
        }
    }
}
