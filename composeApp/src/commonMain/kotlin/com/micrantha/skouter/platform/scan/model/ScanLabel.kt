package com.micrantha.skouter.platform.scan.model

expect class ScanLabel

expect val ScanLabel.label: String
expect val ScanLabel.score: Float

typealias ScanLabels = List<ScanLabel>