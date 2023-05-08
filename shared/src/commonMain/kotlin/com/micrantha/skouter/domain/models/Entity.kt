package com.micrantha.skouter.domain.models

import kotlinx.datetime.Instant

interface Entity {
    val id: String
    val nodeId: String
}

interface Creatable {
    val createdAt: Instant
}
