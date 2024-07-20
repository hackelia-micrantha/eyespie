package com.micrantha.eyespie.domain.model

import kotlinx.datetime.Instant

interface Entity {
    val id: String
}

interface Creatable {
    val createdAt: Instant
}
