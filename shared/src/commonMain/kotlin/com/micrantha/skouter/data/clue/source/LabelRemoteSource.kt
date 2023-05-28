package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.data.clue.model.RecognitionResponse
import com.micrantha.skouter.data.remote.MicranthaClient
import io.github.aakira.napier.Napier
import io.ktor.client.call.*

class LabelRemoteSource(
    private val micranthaClient: MicranthaClient
) {
    suspend fun recognize(image: ByteArray, contentType: String) = try {
        val response: RecognitionResponse = micranthaClient.recognize(image, contentType).body()
        Result.success(response)
    } catch (err: Throwable) {
        Napier.e("recognize", err)
        Result.failure(err)
    }

}
