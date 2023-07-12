package com.micrantha.skouter.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.micrantha.skouter.domain.model.Session
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import io.ktor.client.HttpClient


@Composable
actual fun generateImageLoader(session: Session?, httpClient: HttpClient): ImageLoader {
    val context = LocalContext.current

    return ImageLoader {
        components {
            setupDefaultComponents(context) {
                httpClient
            }
        }
    }
}
