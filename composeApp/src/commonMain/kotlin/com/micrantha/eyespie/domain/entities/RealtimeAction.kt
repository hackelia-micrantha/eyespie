package com.micrantha.eyespie.domain.entities

sealed class RealtimeAction<T> {
    data class Add<T>(val data: T) : RealtimeAction<T>()
    data class Remove<T>(val data: T) : RealtimeAction<T>()
    data class Modify<T>(val data: T, val prev: T) : RealtimeAction<T>()
    data class Query<T>(val data: T) : RealtimeAction<T>()
}
