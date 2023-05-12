package com.micrantha.skouter

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

@Composable
fun UIShow() = SkouterApp(iosModules())

public fun MainViewController(): UIViewController = ComposeUIViewController { UIShow() }
