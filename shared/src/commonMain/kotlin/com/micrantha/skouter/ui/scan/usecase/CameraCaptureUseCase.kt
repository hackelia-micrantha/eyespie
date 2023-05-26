package com.micrantha.skouter.ui.scan.usecase

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import dev.icerock.moko.media.compose.toImageBitmap
import dev.icerock.moko.media.picker.MediaPickerController
import dev.icerock.moko.media.picker.MediaSource.CAMERA

class CameraCaptureUseCase(
    private val mediaPickerController: MediaPickerController
) {
    suspend operator fun invoke(): Result<Painter> = try {
        val result = mediaPickerController.pickImage(CAMERA)
        Result.success(BitmapPainter(result.toImageBitmap()))
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
