package com.micrantha.eyespie.platform.scan.components

import com.micrantha.eyespie.platform.scan.CameraImage


typealias CameraScannerDispatch = suspend (CameraImage) -> Unit
