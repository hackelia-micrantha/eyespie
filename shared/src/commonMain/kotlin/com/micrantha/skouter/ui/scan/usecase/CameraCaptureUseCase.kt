package com.micrantha.skouter.ui.scan.usecase

import dev.icerock.moko.media.picker.MediaPickerController
import dev.icerock.moko.media.picker.MediaSource.CAMERA

class CameraCaptureUseCase(
    private val mediaPickerController: MediaPickerController
) {
    suspend operator fun invoke(): Result<ByteArray> = try {
        val result = mediaPickerController.pickImage(CAMERA)
        Result.success(result.toByteArray())
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
