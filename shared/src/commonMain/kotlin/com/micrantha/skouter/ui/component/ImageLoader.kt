package com.micrantha.skouter.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.domain.model.Session
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupBase64Components
import com.seiko.imageloader.component.setupCommonComponents
import com.seiko.imageloader.component.setupKtorComponents
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.kodein.di.compose.rememberInstance

fun authorizedImageLoader(
    session: Session?
): ImageLoader {
    return ImageLoader {
        components {
            setupCommonComponents()
            setupBase64Components()
            setupKtorComponents {
                HttpClient(CIO) {


                    if (session != null) {

                        defaultRequest {
                            headers {
                                "apiKey" to session.accessToken
                                HttpHeaders.Authorization to "Bearer ${session.accessToken}"
                            }
                        }

                        install(Auth) {
                            bearer {
                                loadTokens {
                                    BearerTokens(
                                        session.accessToken,
                                        session.refreshToken
                                    )
                                }
                            }
                        }
                    }

                    install(Logging) {
                        logger = object : Logger {
                            override fun log(message: String) {
                                Napier.v("ImageLoader", null, message)
                            }
                        }
                        level = LogLevel.ALL
                    }

                }.apply {
                    Napier.base(DebugAntilog())
                }
            }
        }

    }
}

@Composable
fun rememberImageLoader(): ImageLoader {
    val client by rememberInstance<CurrentSession>()

    val session by client.asStateFlow().collectAsState()

    return authorizedImageLoader(session)
}
