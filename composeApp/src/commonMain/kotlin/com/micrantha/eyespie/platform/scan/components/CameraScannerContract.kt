package com.micrantha.eyespie.platform.scan.components

import com.micrantha.eyespie.domain.model.Clue
import com.micrantha.eyespie.platform.scan.CameraImage


typealias CameraScannerDispatch = suspend (CameraImage) -> Unit

typealias ClueScannerDispatch = suspend (Clue<*>) -> Unit

