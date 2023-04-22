package com.micrantha.skouter

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.micrantha.bluebell.Platform
import com.micrantha.bluebell.bluebellModules
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.UIKit.UIViewController

fun initKoin() {
    startKoin {
        modules(module {
            single { Platform() }
        }, bluebellModules(), skouterModules())
    }
}

object Modules : KoinComponent

@Composable
fun UIShow() = App(Modules.getKoin())

public fun MainViewController(): UIViewController = ComposeUIViewController { UIShow() }
