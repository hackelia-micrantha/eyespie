package com.micrantha.skouter.data.client

import com.micrantha.bluebell.data.Log
import com.micrantha.skouter.SkouterConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.DefaultRequest.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
fun createHttpClient(block: DefaultRequestBuilder.() -> Unit) = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v(message = message, tag = "ImageLoader")
            }
        }
        level = LogLevel.HEADERS
    }
    defaultRequest {
        block(this)
    }
}

class MicranthaClient {

    private val httpClient by lazy {
        createHttpClient {
            headers {
                "apiKey" to SkouterConfig.apiKey
            }
        }
    }

    suspend fun recognize(data: ByteArray, contentType: String) =
        httpClient.submitFormWithBinaryData(
            url = "https://recognition.",//${SkouterConfig.apiDomain}",
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
