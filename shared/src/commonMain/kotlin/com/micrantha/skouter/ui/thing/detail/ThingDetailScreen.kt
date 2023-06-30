package com.micrantha.skouter.ui.thing.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch

class ThingDetailScreen(
    private val id: String,
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ThingDetailScreenModel>()

        val state by screenModel.state.collectAsState()

        Render(state, screenModel::dispatch)
    }

    @Composable
    private fun Render(state: ThingDetailUiState, dispatch: Dispatch) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}
