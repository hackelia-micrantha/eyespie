package com.micrantha.skouter.platform

import androidx.compose.runtime.Composable
import com.micrantha.skouter.domain.model.Session
import com.seiko.imageloader.ImageLoader
import io.ktor.client.HttpClient

@Composable
expect fun generateImageLoader(session: Session?, httpClient: HttpClient): ImageLoader
