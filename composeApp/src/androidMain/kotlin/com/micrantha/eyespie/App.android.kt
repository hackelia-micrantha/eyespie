package com.micrantha.eyespie

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.micrantha.eyespie.app.EyesPieApp


@Composable
fun UIShow() {

    EyesPieApp(
        androidDependencies(
            LocalContext.current
        )
    )
}
