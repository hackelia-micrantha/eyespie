package com.micrantha.skouter.platform.scan.model

import org.tensorflow.lite.support.label.Category

actual typealias ScanLabel = Category

actual val ScanLabel.label: String
    get() = this.label

actual val ScanLabel.score: Float
    get() = this.score
