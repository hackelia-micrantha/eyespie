package com.micrantha.skouter.ui.scan.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.ui.component.ChoiceField
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.Init
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.LabelChanged

class ScanEditScreen(
    private val proof: Proof
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: ScanEditScreenModel = rememberScreenModel()

        LaunchedEffect(Unit) {
            viewModel.dispatch(Init(proof))
        }

        val state by viewModel.state.collectAsState()

        Render(state, viewModel::dispatch)
    }

    @Composable
    private fun Render(state: ScanEditUiState, dispatch: Dispatch) {

        Column(
            modifier = Modifier.fillMaxSize().padding(Dimensions.screen)
        ) {
            ChoiceField(
                selected = { Text(text = it.label) },
                choices = state.labels
            ) { choice ->
                dispatch(LabelChanged(choice))
            }
        }
    }

}
