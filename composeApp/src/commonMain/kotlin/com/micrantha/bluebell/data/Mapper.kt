package com.micrantha.bluebell.data

fun interface Mapper<in Input, out Output> {
    operator fun invoke(input: Input): Output
}