package com.micrantha.eyespie.data.clue.source

import com.micrantha.eyespie.AppConfig.HUGGINGFACE_TOKEN
import com.micrantha.eyespie.data.client.createHttpClient
import com.micrantha.eyespie.data.clue.model.ImageResponse
import com.micrantha.eyespie.platform.scan.CameraImage
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.URLProtocol

class LabelRemoteSource {
    private val client by lazy {
        createHttpClient {
            url {
                protocol = URLProtocol.HTTPS
                host = "api-inference.huggingface.co/models/google/vit-base-patch16-224"
            }
            bearerAuth(HUGGINGFACE_TOKEN)
        }
    }

    suspend fun analyze(image: CameraImage) = try {
        Result.success(client.post {
            setBody(image.toByteArray())
        }.body<ImageResponse>())
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
