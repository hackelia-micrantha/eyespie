package com.micrantha.skouter.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.micrantha.bluebell.data.Log
import com.micrantha.skouter.SkouterConfig
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.domain.model.Session
import com.micrantha.skouter.platform.generateImageLoader
import com.seiko.imageloader.ImageLoader
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel.INFO
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.http.headers
import io.ktor.http.path
import org.kodein.di.compose.rememberInstance

private fun Session?.authorizedHttpClient() = HttpClient(CIO) {

    defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = SkouterConfig.supaBaseDomain
            path("storage", "v1")
        }

        if (this@authorizedHttpClient != null) {
            headers {
                "apiKey" to accessToken
            }
        }
    }

    if (this@authorizedHttpClient != null) {
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(accessToken, refreshToken)
                }
            }
        }
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v(message, null, "ImageLoader")
            }
        }
        level = INFO
    }

}

@Composable
fun rememberImageLoader(): ImageLoader {
    val session by rememberInstance<CurrentSession>()

    val data by session.asStateFlow().collectAsState()

    val client = remember { data.authorizedHttpClient() }

    val loader = generateImageLoader(data, client)

    return remember { loader }
}
