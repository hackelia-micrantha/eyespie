package com.micrantha.bluebell.ui.model

import com.micrantha.bluebell.domain.entities.LocalizedString
import com.micrantha.bluebell.ui.model.UiResult.Busy
import com.micrantha.bluebell.ui.model.UiResult.Default
import com.micrantha.bluebell.ui.model.UiResult.Empty
import com.micrantha.bluebell.ui.model.UiResult.Failure
import com.micrantha.bluebell.ui.model.UiResult.Ready
import org.jetbrains.compose.resources.StringResource

sealed class UiResult<out T> {

    data class Ready<T>(val data: T) : UiResult<T>()

    object Default : UiResult<Nothing>()

    data class Busy(val message: LocalizedString? = null) : UiResult<Nothing>()

    data class Failure(val message: LocalizedString? = null) : UiResult<Nothing>()

    data class Empty(val message: LocalizedString? = null) : UiResult<Nothing>()

}

fun Ready() = Ready(Unit)

val <T> UiResult<T>.value: T?
    get() = when (this) {
        is Ready -> data
        else -> null
    }

val <T> UiResult<T>.isReady: Boolean
    get() = this is Ready

val <T> UiResult<T>.isFailure: Boolean
    get() = this is Failure

val <T> UiResult<T>.error: StringResource?
    get() = when (this) {
        is Failure -> message
        else -> null
    }

val <T> UiResult<T>.message: StringResource?
    get() = when (this) {
        is Busy -> message
        is Failure -> message
        is Empty -> message
        else -> null
    }

fun <T> UiResult<T>.copy(ready: (T) -> T) = when (this) {
    is Ready -> this.copy(data = ready(this.data))
    else -> this
}

fun <T, V> UiResult<T>.map(ready: (T) -> V): UiResult<V> = when (this) {
    is Ready -> Ready(ready(data))
    is Busy -> Busy(message)
    is Failure -> Failure(message)
    is Empty -> Empty(message)
    is Default -> Default
}

fun <T, V> UiResult<T>.mapNotNull(block: (T) -> V?): UiResult<V> = when (this) {
    is Ready -> block(data)?.let { Ready(it) } ?: Default
    is Busy -> Busy(message)
    is Failure -> Failure(message)
    is Empty -> Empty(message)
    is Default -> Default
}
