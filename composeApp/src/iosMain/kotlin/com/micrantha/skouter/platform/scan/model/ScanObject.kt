package com.micrantha.skouter.platform.scan.model

import androidx.compose.ui.geometry.Rect
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetMinX
import platform.CoreGraphics.CGRectGetMinY
import platform.CoreGraphics.CGRectGetWidth
import platform.Vision.VNClassificationObservation
import platform.Vision.VNRecognizedObjectObservation

actual typealias ScanObject = VNRecognizedObjectObservation

@OptIn(ExperimentalForeignApi::class)
actual val ScanObject.rect: Rect
    get() = Rect(
        CGRectGetMinX(boundingBox).toFloat(),
        CGRectGetMinY(boundingBox).toFloat(),
        CGRectGetWidth(boundingBox).toFloat(),
        CGRectGetHeight(boundingBox).toFloat()
    )

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual val ScanObject.labels: ScanLabels
    get() = this.labels.filterIsInstance<VNClassificationObservation>()