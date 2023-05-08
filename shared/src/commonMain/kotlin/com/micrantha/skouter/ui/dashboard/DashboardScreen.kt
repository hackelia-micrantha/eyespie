package com.micrantha.skouter.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.micrantha.bluebell.domain.arch.Dispatch

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    val state by viewModel.state().collectAsState()

    DashboardContent(state, viewModel::dispatch)
}

@Composable
fun DashboardContent(
    state: DashboardState,
    dispatch: Dispatch
) {
    Column() {

    }
}
