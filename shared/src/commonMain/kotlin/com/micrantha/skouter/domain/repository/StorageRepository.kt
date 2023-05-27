package com.micrantha.skouter.domain.repository

interface StorageRepository {
    suspend fun download(path: String): Result<ByteArray>

    suspend fun url(path: String): Result<String>

    suspend fun upload(path: String, data: ByteArray): Result<String>

    fun get(path: String): Result<ByteArray>
}
