package com.micrantha.skouter.data.storage.source

import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration.Companion.days

class StorageLocalSource {
    private val cache: Cache<String, ByteArray> = Cache.Builder<String, ByteArray>()
        .expireAfterAccess(7.days)
        .build()

    fun get(key: String): Result<ByteArray> = try {
        Result.success(cache.get(key)!!)
    } catch (err: Throwable) {
        Result.failure(err)
    }

    fun put(key: String, data: ByteArray) {
        cache.put(key, data)
    }
}
