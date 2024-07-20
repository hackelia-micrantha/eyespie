package com.micrantha.eyespie.ui.scan.guess

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.eyespie.platform.scan.CameraScanner
import com.micrantha.eyespie.ui.scan.guess.ScanGuessAction.ImageCaptured
import dev.icerock.moko.permissions.Permission.CAMERA
import dev.icerock.moko.permissions.PermissionsController
import org.kodein.di.compose.rememberInstance

class ScanGuessScreen constructor(
    private val args: ScanGuessArgs
) : Screen {

    constructor(id: String) : this(ScanGuessArgs(id))

    @Composable
    override fun Content() {
        val permissions by rememberInstance<PermissionsController>()

        val screenModel: ScanGuessScreenModel = rememberScreenModel(arg = args)

        LaunchedEffect(Unit) {
            permissions.providePermission(CAMERA)
        }

        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
    }

    @Composable
    private fun Render(state: ScanGuessUiState, dispatch: Dispatch) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CameraScanner(
                modifier = Modifier.align(Alignment.TopCenter).fillMaxSize(),
            ) {
                dispatch.send(ImageCaptured(it))
            }
        }
    }

    @Composable
    private fun BoxScope.Matched() {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {}
    }
}
