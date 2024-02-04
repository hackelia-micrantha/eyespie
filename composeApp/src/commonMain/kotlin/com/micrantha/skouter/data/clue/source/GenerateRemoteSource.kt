package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.data.client.huggingFaceClient
import com.micrantha.skouter.data.clue.model.GenerateImageRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.readBytes

class GenerateRemoteSource {
    private val client by lazy {
        huggingFaceClient("stabilityai/stable-diffusion-xl-base-1.0")
    }

    suspend fun generate(request: GenerateImageRequest) = try {
        Result.success(client.post {
            setBody(request)
        }.readBytes())
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
