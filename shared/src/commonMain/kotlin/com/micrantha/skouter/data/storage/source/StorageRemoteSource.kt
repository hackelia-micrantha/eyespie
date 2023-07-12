package com.micrantha.skouter.data.storage.source

import com.micrantha.bluebell.data.Log
import com.micrantha.skouter.data.client.SupaClient
import kotlin.time.Duration.Companion.days

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
        Log.e("image", err)
        Result.failure(err)
    }

    suspend fun upload(
        bucketId: String,
        path: String,
        data: ByteArray
    ): Result<Pair<String, String>> = try {
        with(supabase.storage(bucketId)) {
            val key = upload(path, data)
            val url = createSignedUrl(path, 365.days)
            Result.success(Pair(key, url))
        }
    } catch (err: Throwable) {
        Log.e("upload", err)
        Result.failure(err)
    }
}
