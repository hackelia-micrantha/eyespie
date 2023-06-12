package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.bluebell.platform.toPainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.Path

class LoadCameraImageUseCase(
    private val fileSystem: FileSystem
) {

    suspend operator fun invoke(path: Path) = dispatchUseCase(
        Dispatchers.IO
    ) {
        fileSystem.read(path).toPainter()
    }
}
