package com.micrantha.eyespie.platform.scan.components

fun interface AnalyzerCallback<in T> {
    fun onAnalyzerResult(result: T)
    fun onAnalyzerError(e: Throwable) = Unit
}
