package com.micrantha.eyespie.ui.scan.usecase

import androidx.compose.ui.graphics.painter.BitmapPainter
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.bluebell.platform.toImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.Path

class GetEditCaptureUseCase(
    private val fileSystem: FileSystem
) {

    suspend operator fun invoke(path: Path) = dispatchUseCase(
        Dispatchers.IO
    ) {
        BitmapPainter(fileSystem.read(path).toImageBitmap())
    }
}
