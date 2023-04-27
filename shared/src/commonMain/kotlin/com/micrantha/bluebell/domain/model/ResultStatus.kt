package com.micrantha.bluebell.domain.model

sealed class ResultStatus {

    data class Ready<T>(val data: T) : ResultStatus()

    object Default : ResultStatus()

    data class Busy(val message: String? = null) : ResultStatus()

    data class Failure(val error: String? = null) : ResultStatus()

    data class Empty(val message: String? = null) : ResultStatus()
}
