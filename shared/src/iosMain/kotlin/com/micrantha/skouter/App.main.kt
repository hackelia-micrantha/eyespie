package com.micrantha.skouter

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.micrantha.bluebell.bluebellModules
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun initKoin() = startKoin {
    modules(
        iosModules(),
        bluebellModules(),
        skouterModules()
    )
}

object Modules : KoinComponent

@Composable
fun UIShow() = SkouterApp(Modules.getKoin().get())

public fun MainViewController(): UIViewController = ComposeUIViewController { UIShow() }
