package com.micrantha.skouter.ui.scan.edit

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel

class ScanEditScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: ScanEditViewScreenModel = rememberScreenModel()
    }

}
