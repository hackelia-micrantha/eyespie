package com.micrantha.skouter.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.micrantha.skouter.domain.model.Session
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.intercept.Interceptor.Chain
import com.seiko.imageloader.model.ImageResult
import io.ktor.client.HttpClient


@Composable
actual fun generateImageLoader(session: Session?, httpClient: HttpClient): ImageLoader {
    val context = LocalContext.current

    return ImageLoader {
        interceptor {
            addInterceptor(object : Interceptor {
                override suspend fun intercept(chain: Chain): ImageResult {
                    return chain.proceed(chain.request)
                }
            })
        }
        components {
            setupDefaultComponents(context) {
                httpClient
            }
        }
    }
}
