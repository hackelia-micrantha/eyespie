package com.micrantha.skouter.platform.scan.components

import com.micrantha.skouter.platform.scan.CameraImage

typealias CameraScannerDispatch = suspend (CameraImage) -> Unit
