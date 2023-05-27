package com.micrantha.skouter.ui.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.model.UiResult.Busy
import com.micrantha.bluebell.domain.model.UiResult.Failure
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.bluebell.ui.components.status.FailureContent
import com.micrantha.bluebell.ui.components.status.LoadingContent
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.ui.scan.ScanAction.Init
import com.micrantha.skouter.ui.scan.ScanAction.SaveScan

class ScanScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: ScanScreenModel = rememberScreenModel()

        LaunchedEffect(viewModel) {
            viewModel.dispatch(Init)
        }

        val state by viewModel.state().collectAsState()

        when (val result = state.status) {
            is Ready -> Render(result.data, viewModel::dispatch)
            is Failure -> FailureContent(result.message)
            is Busy -> LoadingContent(result.message)
            else -> LoadingContent()
        }
    }

    @Composable
    private fun Render(data: ScanUiState.Data, dispatch: Dispatch) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                painter = data.image,
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )
            Card(
                modifier = Modifier.fillMaxWidth().padding(Dimensions.screen)
                    .heightIn(200.dp)
            ) {
                Text("clues")
            }
            Button(
                modifier = Modifier.fillMaxWidth().padding(Dimensions.content),
                onClick = { dispatch(SaveScan) }
            ) {
                Text("Save")
            }
        }
    }
}
