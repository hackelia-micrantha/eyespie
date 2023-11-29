package com.micrantha.skouter.com.micrantha.skouter

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.micrantha.skouter.SkouterApp
import com.micrantha.skouter.androidDependencies


@Composable
fun UIShow() {

    SkouterApp(
        androidDependencies(
            LocalContext.current,
        )
    )
}
