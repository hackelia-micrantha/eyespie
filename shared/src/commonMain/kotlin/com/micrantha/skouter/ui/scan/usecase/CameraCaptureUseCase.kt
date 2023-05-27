package com.micrantha.skouter.ui.scan.usecase

import com.benasher44.uuid.uuid4
import com.micrantha.bluebell.Platform
import dev.icerock.moko.media.picker.MediaPickerController
import dev.icerock.moko.media.picker.MediaSource.CAMERA
import okio.FileSystem

class CameraCaptureUseCase(
    private val platform: Platform,
    private val mediaPickerController: MediaPickerController
) {
    suspend operator fun invoke() = try {
        val result = mediaPickerController.pickImage(CAMERA)
        val data = result.toByteArray()
        val path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.div(uuid4().toString())
        platform.write(path, data)
        Result.success(path)
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
