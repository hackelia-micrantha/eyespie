package com.micrantha.skouter.ui.scan.usecase

import com.benasher44.uuid.uuid4
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.domain.repository.MatchRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedMatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.FileSystem
import kotlin.coroutines.coroutineContext

class TakeCaptureUseCase(
    private val platform: Platform,
    private val matchRepository: MatchRepository,
    private val dispatcher: Dispatcher,
) : Dispatcher by dispatcher {
    suspend operator fun invoke(image: CameraImage) = dispatchUseCase(coroutineContext) {
        matchRepository.analyze(image).onSuccess {
            dispatch(ScannedMatch(it))
        }.onFailure {
            throw it
        }

        withContext(Dispatchers.IO) {
            FileSystem.SYSTEM_TEMPORARY_DIRECTORY.div(uuid4().toString()).apply {
                platform.write(this, image.toByteArray())
            }
        }
    }
}
