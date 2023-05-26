package com.micrantha.skouter.ui.thing.usecase

import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.bluebell.ui.toPainter
import com.micrantha.skouter.domain.model.Image
import com.micrantha.skouter.domain.repository.StorageRepository
import io.github.aakira.napier.Napier

class LoadImageFromStorageUseCase(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(image: Image): Result<Painter> {
        return storageRepository.download(image)
            .onFailure {
                Napier.e("load image failed - ${image.path}", it)
            }.map { it.toPainter() }
    }
}
