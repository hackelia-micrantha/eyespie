package com.micrantha.eyespie.domain.repository

interface StorageRepository {
    suspend fun download(path: String): Result<ByteArray>

    suspend fun upload(path: String, data: ByteArray): Result<String>

    fun get(path: String): Result<ByteArray>
}
