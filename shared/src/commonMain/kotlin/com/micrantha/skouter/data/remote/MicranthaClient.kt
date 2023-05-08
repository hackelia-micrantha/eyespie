package com.micrantha.skouter.data.remote

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

private const val MICRANTHA_DOMAIN = "micrantha.com"

class MicranthaClient {

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun recognize(data: ByteArray, contentType: String) =
        httpClient.submitFormWithBinaryData(
            url = "https:${MICRANTHA_DOMAIN}/recognize",
            formData {
                append(
                    "image",
                    data,
                    HeadersBuilder().apply {
                        append(HttpHeaders.ContentType, contentType)
                    }.build()
                )
            }
        )
}
