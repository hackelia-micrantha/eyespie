package com.micrantha.skouter.ui.scan.usecase

import com.benasher44.uuid.uuid4
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.platform.CameraImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.FileSystem

class CameraCaptureUseCase(
    private val platform: Platform,
) {
    suspend operator fun invoke(image: CameraImage) = dispatchUseCase(
        Dispatchers.IO
    ) {
        val path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.div(uuid4().toString())
        val rotatedImage = image.rotate()
        platform.write(path, rotatedImage.toByteArray())
        path
    }
}
