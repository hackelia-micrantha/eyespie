package com.micrantha.skouter.ui.scan.preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.platform.CameraScanner
import com.micrantha.skouter.ui.component.LocationEnabledEffect
import com.micrantha.skouter.ui.scan.preview.ScanAction.EditScan
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

        Render(state, viewModel::dispatch)
    }

    @OptIn(ExperimentalTextApi::class)
    @Composable
    private fun Render(state: ScanUiState, dispatch: Dispatch) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CameraScanner(
                modifier = Modifier.align(Alignment.TopCenter).fillMaxSize(),
                state.enabled,
                dispatch
            )

            state.current?.let { (rect, label) ->
                ScannedBox(
                    rect = rect,
                    label = label,
                    imageSize = state.imageSize
                )
            }

            if (state.clues.isEmpty().not()) {
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

    @OptIn(ExperimentalTextApi::class)
    @Composable
    private fun BoxScope.ScannedBox(
        modifier: Modifier = Modifier,
        rect: Rect,
        label: String,
        imageSize: Size
    ) {

        val measurer = rememberTextMeasurer()

        val textStyle = MaterialTheme.typography.labelMedium.copy(color = Color.Red)

        val matrix = remember { Matrix() }

        Canvas(
            modifier = modifier.align(Alignment.TopCenter).fillMaxSize()
        ) {
            matrix.reset()

            matrix.scale(
                size.width / imageSize.width,
                size.height / imageSize.height
            )
            //matrix.translate(size.width, size.height)

            val bounds = matrix.map(rect)

            drawOutline(
                outline = Outline.Rectangle(bounds),
                color = Color.Red,
                style = Stroke(1f)
            )
            drawText(
                textMeasurer = measurer,
                text = label,
                topLeft = bounds.topLeft,
                style = textStyle
            )
        }
    }

    @Composable
    private fun ScannedClues(modifier: Modifier, clues: Set<Clue<*>>) {
        Surface(
            modifier = modifier.padding(Dimensions.screen)
        ) {
            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                clues.forEach {
                    Text(
                        text = it.display(),
                        maxLines = 1,
                        modifier = Modifier.padding(Dimensions.content)
                    )
                }
            }
        }
    }
}
