package com.micrantha.skouter.data.remote

import Skouter.shared.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class MicranthaClient {

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            headers {
                "apiKey" to BuildConfig.apiKey
            }
        }
    }

    suspend fun recognize(data: ByteArray, contentType: String) =
        httpClient.submitFormWithBinaryData(
            url = "https://recognition.${BuildConfig.apiDomain}",
            formData {
                append(
                    "image",
                    data,
                    headers {
                        HttpHeaders.ContentType to contentType
                    }
                )
            }
        )
}
