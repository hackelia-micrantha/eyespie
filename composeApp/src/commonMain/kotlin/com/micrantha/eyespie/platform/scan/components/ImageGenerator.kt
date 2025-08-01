package com.micrantha.eyespie.platform.scan.components

import com.micrantha.eyespie.platform.scan.CameraImage

interface ImageGenerator<in T> {
    suspend fun generate(from: T): Result<CameraImage>
}