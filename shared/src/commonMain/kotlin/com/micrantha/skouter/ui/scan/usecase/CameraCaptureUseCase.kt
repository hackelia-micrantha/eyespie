package com.micrantha.skouter.ui.scan.usecase

import com.benasher44.uuid.uuid4
import com.micrantha.bluebell.platform.Platform
import okio.FileSystem

class CameraCaptureUseCase(
    private val platform: Platform,
) {
    operator fun invoke(data: ByteArray) = try {
        val path = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.div(uuid4().toString())
        platform.write(path, data)
        Result.success(path)
    } catch (err: Throwable) {
        Result.failure(err)
    }
}
