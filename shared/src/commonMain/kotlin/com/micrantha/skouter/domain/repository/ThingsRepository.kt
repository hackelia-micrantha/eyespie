package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.models.Clues

interface ThingsRepository {

    suspend fun recognize(image: ByteArray, contentType: String): Result<Clues>
}
