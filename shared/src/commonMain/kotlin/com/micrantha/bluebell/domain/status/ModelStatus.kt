package com.micrantha.bluebell.domain.status

sealed class ModelStatus {

    data class Ready<T>(val data: T) : ModelStatus()

    data class Busy(val message: String? = null) : ModelStatus()

    data class Failure(val error: String? = null) : ModelStatus()

    data class Empty(val message: String? = null) : ModelStatus()
}
