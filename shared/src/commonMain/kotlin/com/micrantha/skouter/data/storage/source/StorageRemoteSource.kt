package com.micrantha.skouter.data.storage.source

import com.micrantha.skouter.data.client.SupaClient
import io.github.aakira.napier.Napier

class StorageRemoteSource(
    private val supabase: SupaClient,
) {

    fun url(bucketID: String, path: String): Result<String> = try {
        val result = supabase.storage(bucketID).authenticatedRenderUrl(path)
        Result.success(result)
    } catch (err: Throwable) {
        Result.failure(err)
    }

    suspend fun download(bucketID: String, path: String): Result<ByteArray> = try {
        val result = supabase.storage(bucketID)
            .downloadAuthenticated(path)
        Result.success(result)
    } catch (err: Throwable) {
        Napier.e("image", err)
        Result.failure(err)
    }

    suspend fun upload(
        bucketId: String,
        path: String,
        data: ByteArray
    ): Result<Pair<String, String>> = try {
        with(supabase.storage(bucketId)) {
            val key = upload(path, data)
            val url = authenticatedRenderUrl(path)
            Result.success(Pair(key, url))
        }
    } catch (err: Throwable) {
        Napier.e("upload", err)
        Result.failure(err)
    }
}
