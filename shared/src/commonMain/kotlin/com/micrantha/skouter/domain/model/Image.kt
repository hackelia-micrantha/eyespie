package com.micrantha.skouter.domain.model

import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.bluebell.domain.model.UiResult

typealias ImageDownload = Painter

data class Image(
    val fileId: String,
    val bucketId: String,
    val playerId: String,
    val status: UiResult<ImageDownload> = UiResult.Default
) {
    val path = "${playerId}/${fileId}"

    val id = "${bucketId}/${path}"
}
