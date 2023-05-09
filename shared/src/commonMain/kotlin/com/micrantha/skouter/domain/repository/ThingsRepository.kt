package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.models.Clues
import com.micrantha.skouter.domain.models.Thing.Image

interface ThingsRepository {

    suspend fun recognize(image: ByteArray, contentType: String): Result<Clues>

    suspend fun image(image: Image): Result<ByteArray>
}
