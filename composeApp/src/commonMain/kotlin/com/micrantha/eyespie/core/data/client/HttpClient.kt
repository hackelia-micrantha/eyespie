package com.micrantha.eyespie.core.data.client

import com.micrantha.bluebell.app.Log
import com.micrantha.eyespie.platform.httpClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest.DefaultRequestBuilder
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
fun createHttpClient(block: DefaultRequestBuilder.() -> Unit) = HttpClient(httpClientEngine()) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.d("ImageLoader") { message }
            }
        }
        level = LogLevel.HEADERS
    }
    defaultRequest {
        block(this)
    }
}
