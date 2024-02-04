package com.micrantha.skouter.data.clue

import androidx.compose.ui.graphics.ImageBitmap
import com.micrantha.bluebell.platform.toImageBitmap
import com.micrantha.skouter.data.clue.model.toImageRequest
import com.micrantha.skouter.data.clue.source.GenerateRemoteSource
import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.domain.repository.GenerateRepository

class GenerateDataRepository(
    private val remoteSource: GenerateRemoteSource
) : GenerateRepository {
    override suspend fun generate(from: Clues): Result<ImageBitmap> {
        return remoteSource.generate(from.toImageRequest())
            .map { it.toImageBitmap() }
    }
}
