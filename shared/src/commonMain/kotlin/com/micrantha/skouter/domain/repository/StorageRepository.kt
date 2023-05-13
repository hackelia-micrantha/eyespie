package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.models.Image

interface StorageRepository {
    suspend fun download(image: Image): Result<ByteArray>

    fun get(image: Image): Result<ByteArray>
}
