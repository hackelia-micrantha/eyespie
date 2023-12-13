package com.micrantha.skouter.data.clue.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull


class RepositoryStore<T> {
    private val data = MutableStateFlow<T?>(null)

    val value: Flow<T> = data.filterNotNull()

    fun update(value: T) {
        data.value = value
    }
}
