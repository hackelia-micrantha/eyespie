package com.micrantha.skouter.platform.scan.components

fun interface AnalyzerCallback<T> {
    fun onAnalyzerResult(result: T)
    fun onAnalyzerError(e: Throwable) = Unit
}
