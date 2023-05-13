package com.micrantha.skouter.data.storage.source

import com.micrantha.skouter.data.remote.SupaClient
import io.github.aakira.napier.Napier

class StorageRemoteSource(
    private val supabase: SupaClient,
) {
    suspend fun download(bucketId: String, path: String): Result<ByteArray> = try {
        val result = supabase.storage(bucketId)
            .downloadAuthenticated(path)
        Result.success(result)
    } catch (err: Throwable) {
        Napier.e("image", err)
        Result.failure(err)
    }
}
