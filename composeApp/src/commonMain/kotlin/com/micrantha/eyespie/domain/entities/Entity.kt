package com.micrantha.eyespie.domain.entities

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface Entity {
    val id: String
}

interface Creatable {
    @OptIn(ExperimentalTime::class)
    val createdAt: Instant
}
