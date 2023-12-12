package com.micrantha.skouter.platform.scan.model

data class ScanColor(
    val name: String,
    val rgb: Int,
)

typealias ScanColors = List<ScanColor>
