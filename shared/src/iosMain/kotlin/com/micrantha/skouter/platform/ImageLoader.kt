package com.micrantha.skouter.platform

import com.micrantha.skouter.domain.model.Session
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import io.ktor.client.HttpClient


actual fun generateImageLoader(session: Session?, httpClient: HttpClient) = ImageLoader {
    components {
        setupDefaultComponents {
            httpClient
        }
    }
}
