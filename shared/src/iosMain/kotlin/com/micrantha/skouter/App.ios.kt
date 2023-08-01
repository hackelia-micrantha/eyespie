package com.micrantha.skouter

import androidx.compose.runtime.Composable
import platform.UIKit.UIViewController

@Composable
fun UIShow(viewController: UIViewController) = SkouterApp(iosModules(viewController))
