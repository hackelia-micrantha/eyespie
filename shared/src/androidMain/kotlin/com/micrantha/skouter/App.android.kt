package com.micrantha.skouter

import androidx.activity.result.ActivityResultCaller
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun UIShow(activityResultCaller: ActivityResultCaller) {
    SkouterApp(androidDependencies(LocalContext.current, activityResultCaller))
}
