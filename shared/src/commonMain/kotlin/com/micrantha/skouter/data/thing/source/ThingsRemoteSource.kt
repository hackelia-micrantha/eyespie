package com.micrantha.skouter.data.thing.source

import com.micrantha.skouter.data.remote.MicranthaClient
import com.micrantha.skouter.data.thing.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.thing.model.RecognitionResponse
import com.micrantha.skouter.domain.models.Clues
import io.github.aakira.napier.Napier
import io.ktor.client.call.*

class ThingsRemoteSource(
    private val client: MicranthaClient,
    private val mapper: ThingsDomainMapper
) {
    suspend fun recognize(image: ByteArray, contentType: String): Result<Clues> =
        try {
            val response: RecognitionResponse = client.recognize(image, contentType).body()
            Result.success(mapper(response))
        } catch (err: Throwable) {
            Napier.e("recognize", err)
            Result.failure(err)
        }

}
