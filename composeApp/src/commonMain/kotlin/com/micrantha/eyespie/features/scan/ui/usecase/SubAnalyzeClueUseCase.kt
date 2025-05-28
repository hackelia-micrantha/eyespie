package com.micrantha.eyespie.features.scan.ui.usecase

import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.eyespie.domain.repository.ColorRepository
import com.micrantha.eyespie.domain.repository.LabelRepository
import com.micrantha.eyespie.platform.scan.CameraImage
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
