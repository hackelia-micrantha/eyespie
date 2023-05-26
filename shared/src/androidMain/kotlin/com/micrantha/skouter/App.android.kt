package com.micrantha.skouter

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun UIShow() {

    SkouterApp(
        androidDependencies(
            LocalContext.current,
        )
    )
}
