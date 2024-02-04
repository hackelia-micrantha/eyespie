package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.data.client.huggingFaceClient
import com.micrantha.skouter.data.clue.model.ImageResponse
import com.micrantha.skouter.platform.scan.CameraImage
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class LabelRemoteSource {
    private val client by lazy {
        huggingFaceClient("google/vit-base-patch16-224")
    }

    suspend fun analyze(image: CameraImage) = try {
        Result.success(client.post {
            setBody(image.toByteArray())
        }.body<ImageResponse>())
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
