package com.micrantha.skouter

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.BluebellApp
import com.micrantha.bluebell.Platform
import com.micrantha.bluebell.ui.components.effects.ScreenVisibilityEffect
import com.micrantha.bluebell.ui.navi.NavigationScreen
import com.micrantha.skouter.ui.MainViewModel
import com.micrantha.skouter.ui.navi.routes
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

@Composable
fun SkouterApp(platform: Platform) = BluebellApp(platform, routes()) { context ->

    val viewModel: MainViewModel by platform.inject { parametersOf(context) }

    ScreenVisibilityEffect(viewModel)

    NavigationScreen(context)
}
