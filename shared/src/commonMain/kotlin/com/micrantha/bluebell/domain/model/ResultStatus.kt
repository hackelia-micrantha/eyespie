package com.micrantha.bluebell.domain.model

import com.micrantha.bluebell.domain.model.ResultStatus.Ready

sealed class ResultStatus<out T> {

    data class Ready<T>(val data: T) : ResultStatus<T>()

    object Default : ResultStatus<Nothing>()

    data class Busy(val message: String? = null) : ResultStatus<Nothing>()

    data class Failure(val message: String? = null) : ResultStatus<Nothing>()

    data class Empty(val message: String? = null) : ResultStatus<Nothing>()
}

fun <T> ResultStatus<T>.copy(ready: (T) -> T) = when (this) {
    is Ready -> this.copy(data = ready(this.data))
    else -> this
}
