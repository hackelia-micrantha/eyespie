package com.micrantha.skouter.platform.scan.model

import androidx.compose.ui.geometry.Rect

expect abstract class ScanObject

expect val ScanObject.rect: Rect
expect val ScanObject.labels: ScanLabels

typealias ScanObjects = List<ScanObject>
