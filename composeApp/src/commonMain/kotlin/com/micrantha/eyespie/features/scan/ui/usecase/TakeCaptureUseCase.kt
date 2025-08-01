package com.micrantha.eyespie.features.scan.ui.usecase

import com.benasher44.uuid.uuid4
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.bluebell.platform.Platform
import com.micrantha.eyespie.domain.repository.MatchRepository
import com.micrantha.eyespie.features.scan.ui.capture.ScanAction.GeneratedImage
import com.micrantha.eyespie.platform.scan.CameraImage
import com.micrantha.eyespie.platform.scan.generator.ImageObfuscator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import okio.FileSystem
import kotlin.coroutines.coroutineContext

class TakeCaptureUseCase(
    private val platform: Platform,
    private val matchRepository: MatchRepository,
    private val imageObfuscator: ImageObfuscator,
    private val dispatcher: Dispatcher,
) : Dispatcher by dispatcher {
    suspend operator fun invoke(image: CameraImage) = dispatchUseCase(coroutineContext) {

        analyze(image)

        withContext(Dispatchers.IO) {
            FileSystem.SYSTEM_TEMPORARY_DIRECTORY.div(uuid4().toString()).apply {
                platform.write(this, image.toByteArray())
            }
        }
    }

    private suspend fun analyze(image: CameraImage) = supervisorScope {
        val tasks = listOf(
            async {
                matchRepository.analyze(image).onSuccess {
                    dispatch(it)
                }
            },
            async {
                imageObfuscator.generate(image).onSuccess {
                    dispatch(GeneratedImage(it))
                }
            }
        )

        tasks.awaitAll()
    }
}
