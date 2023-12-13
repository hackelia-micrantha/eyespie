package com.micrantha.skouter.data.clue.source

import com.micrantha.skouter.SkouterConfig.huggingFaceApiToken
import com.micrantha.skouter.data.client.createHttpClient
import com.micrantha.skouter.data.clue.model.ImageResponse
import com.micrantha.skouter.platform.scan.CameraImage
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
            bearerAuth(huggingFaceApiToken)
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
