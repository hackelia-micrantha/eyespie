package com.micrantha.eyespie

import IOSApplication
import androidx.compose.runtime.Composable
import platform.UIKit.UIView
import platform.UIKit.UIViewController

interface UIApplicationController {
    fun createViewController(): UIViewController

    fun update(viewController: UIViewController)

    fun finish(viewController: UIViewController)

    val currentView: UIView
}

@Composable
fun UIShow(app: IOSApplication) = EyesPieApp(iosModules(app))
