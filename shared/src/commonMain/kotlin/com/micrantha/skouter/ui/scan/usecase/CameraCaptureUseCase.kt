package com.micrantha.skouter.ui.scan.usecase

import com.benasher44.uuid.uuid4
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.domain.repository.ClueRepository
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedMatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.FileSystem

class CameraCaptureUseCase(
    private val platform: Platform,
    private val clueRepository: ClueRepository,
    private val dispatcher: Dispatcher,
) : Dispatcher by dispatcher {
    suspend operator fun invoke(image: CameraImage) = dispatchUseCase(
        Dispatchers.IO
    ) {
        val rotatedImage = image.rotate()
        clueRepository.match(image).onSuccess {
            dispatch(ScannedMatch(it))
        }.onFailure {
            throw it
        }

        FileSystem.SYSTEM_TEMPORARY_DIRECTORY.div(uuid4().toString()).apply {
            platform.write(this, rotatedImage.toByteArray())
        }
    }
}
