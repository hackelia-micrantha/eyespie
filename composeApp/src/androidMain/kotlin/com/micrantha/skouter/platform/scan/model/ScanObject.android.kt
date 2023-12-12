package com.micrantha.skouter.platform.scan.model

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toComposeRect
import androidx.core.graphics.toRect
import org.tensorflow.lite.task.vision.detector.Detection

actual typealias ScanObject = Detection

actual val Detection.rect: Rect
    get() = this.boundingBox.toRect().toComposeRect()

actual val Detection.labels: ScanLabels
    get() = this.categories