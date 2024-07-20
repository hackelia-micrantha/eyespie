package com.micrantha.eyespie

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@Composable
fun UIShow() {

    EyesPieApp(
        androidDependencies(
            LocalContext.current,
        )
    )
}
