package com.micrantha.skouter.domain.models

import kotlinx.datetime.LocalDateTime

interface Entity {
    val id: String
}

interface Creatable {
    val createdAt: LocalDateTime
}
