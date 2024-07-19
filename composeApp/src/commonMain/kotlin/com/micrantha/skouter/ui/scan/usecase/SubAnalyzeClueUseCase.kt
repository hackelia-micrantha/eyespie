package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.skouter.domain.repository.ColorRepository
import com.micrantha.skouter.domain.repository.LabelRepository
import com.micrantha.skouter.platform.scan.CameraImage
import kotlin.coroutines.coroutineContext

class SubAnalyzeClueUseCase(
    private val colorRepository: ColorRepository,
    private val labelRepository: LabelRepository,
    private val dispatcher: Dispatcher
) : Dispatcher by dispatcher {

    suspend operator fun invoke(image: CameraImage) = dispatchUseCase(coroutineContext) {
        labelRepository.analyze(image).onSuccess { proof ->
            proof.forEach { dispatch(it) }
        }
        colorRepository.analyze(image).onSuccess { proof ->
            proof.forEach { dispatch(it) }
        }
    }
}
