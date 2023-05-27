package com.micrantha.skouter.ui.components

import com.micrantha.skouter.data.remote.SupaClient
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupBase64Components
import com.seiko.imageloader.component.setupCommonComponents
import com.seiko.imageloader.component.setupKtorComponents
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*

fun authorizedImageLoader(
    supaClient: SupaClient
): ImageLoader {
    val token = supaClient.auth().currentAccessTokenOrNull()

    return ImageLoader {
        components {
            setupCommonComponents()
            setupBase64Components()
            setupKtorComponents {
                HttpClient(CIO) {
                    install(Logging) {
                        logger = object : Logger {
                            override fun log(message: String) {
                                Napier.v("ImageLoader", null, message)
                            }
                        }
                        level = LogLevel.HEADERS
                    }
                }.also { Napier.base(DebugAntilog()) }
            }
        }

        interceptor {
            headers {
                HttpHeaders.Authorization to "Bearer $token"
            }
        }
    }
}
