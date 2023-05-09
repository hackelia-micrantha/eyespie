package com.micrantha.bluebell.domain.model

import com.micrantha.bluebell.domain.model.UiResult.Ready

sealed class UiResult<out T> {

    data class Ready<T>(val data: T) : UiResult<T>()

    object Default : UiResult<Nothing>()

    data class Busy(val message: String? = null) : UiResult<Nothing>()

    data class Failure(val message: String? = null) : UiResult<Nothing>()

    data class Empty(val message: String? = null) : UiResult<Nothing>()
}

fun <T> UiResult<T>.copy(ready: (T) -> T) = when (this) {
    is Ready -> this.copy(data = ready(this.data))
    else -> this
}
