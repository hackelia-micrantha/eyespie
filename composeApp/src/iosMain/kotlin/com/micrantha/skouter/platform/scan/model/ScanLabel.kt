package com.micrantha.skouter.platform.scan.model

import platform.Vision.VNClassificationObservation

actual typealias ScanLabel = VNClassificationObservation

actual val ScanLabel.label: String
    get() = this.identifier

actual val ScanLabel.score: Float
    get() = this.confidence
