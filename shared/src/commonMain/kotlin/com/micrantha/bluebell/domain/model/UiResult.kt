package com.micrantha.bluebell.domain.model

import com.micrantha.bluebell.domain.model.UiResult.Busy
import com.micrantha.bluebell.domain.model.UiResult.Default
import com.micrantha.bluebell.domain.model.UiResult.Empty
import com.micrantha.bluebell.domain.model.UiResult.Failure
import com.micrantha.bluebell.domain.model.UiResult.Ready

sealed class UiResult<out T> {

    data class Ready<T>(val data: T) : UiResult<T>()

    object Default : UiResult<Nothing>()

    data class Busy(val message: String? = null) : UiResult<Nothing>()

    data class Failure(val message: String? = null) : UiResult<Nothing>()

    data class Empty(val message: String? = null) : UiResult<Nothing>()

}

fun Ready() = Ready(Unit)

fun <T> UiResult<T>.copy(ready: (T) -> T) = when (this) {
    is Ready -> this.copy(data = ready(this.data))
    else -> this
}

fun <T, V> UiResult<T>.map(ready: (T) -> V): UiResult<V> = when (this) {
    is Ready -> Ready(ready(data))
    is Busy -> Busy()
    is Failure -> Failure()
    is Empty -> Empty()
    is Default -> Default
}

fun <T, V> UiResult<T>.mapNotNull(block: (T) -> V?): UiResult<V> = when (this) {
    is Ready -> block(data)?.let { Ready(it) } ?: Default
    is Busy -> Busy()
    is Failure -> Failure()
    is Empty -> Empty()
    is Default -> Default
}
