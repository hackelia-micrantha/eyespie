package com.micrantha.eyespie

import androidx.compose.runtime.Composable
import com.micrantha.eyespie.app.EyesPieApp
import platform.UIKit.UIView
import platform.UIKit.UIViewController

interface UIApplicationController {
    fun createViewController(): UIViewController

    fun update(viewController: UIViewController)

    fun finish(viewController: UIViewController)

    val currentView: UIView
}

@Composable
fun UIShow(app: AppDelegate) = EyesPieApp(iosModules(app))
