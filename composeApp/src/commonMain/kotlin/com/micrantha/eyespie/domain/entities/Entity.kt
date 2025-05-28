package com.micrantha.eyespie.domain.entities

import kotlinx.datetime.Instant

interface Entity {
    val id: String
}

interface Creatable {
    val createdAt: Instant
}
