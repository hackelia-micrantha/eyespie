package com.micrantha.skouter.platform.scan

interface CaptureAnalyzer<T> {
    suspend fun analyze(image: CameraImage): Result<T>
}

interface StreamAnalyzer {
    fun analyze(image: CameraImage)
}

typealias CameraScannerDispatch = suspend (CameraImage) -> Unit

fun interface AnalyzerCallback<T> {
    fun onAnalyzerResult(result: T)

    fun onAnalyzerError(e: Throwable) = Unit
}
