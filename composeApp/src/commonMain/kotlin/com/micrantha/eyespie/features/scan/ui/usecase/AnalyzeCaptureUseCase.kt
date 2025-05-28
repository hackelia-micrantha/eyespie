package com.micrantha.eyespie.features.scan.ui.usecase

import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.eyespie.domain.entities.Clue
import com.micrantha.eyespie.domain.repository.ColorRepository
import com.micrantha.eyespie.domain.repository.DetectRepository
import com.micrantha.eyespie.domain.repository.LabelRepository
import com.micrantha.eyespie.domain.repository.SegmentRepository
import com.micrantha.eyespie.platform.scan.CameraImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AnalyzeCaptureUseCase(
    private val labelRepository: LabelRepository,
    private val detectionRepository: DetectRepository,
    private val segmentRepository: SegmentRepository,
    private val colorRepository: ColorRepository,
    private val dispatcher: Dispatcher
) : Dispatcher by dispatcher {

    private val _clues = MutableSharedFlow<Action>(replay = 4)

    val clues = _clues.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(image: CameraImage) = channelFlow<Collection<Clue<*>>> {
        launch {
            send(labelRepository.analyze(image).getOrThrow())
        }
        launch {
            send(detectionRepository.analyze(image).getOrThrow())
        }
        launch {
            send(segmentRepository.analyze(image).getOrThrow())
        }
        launch {
            send(colorRepository.analyze(image).getOrThrow())
        }
    }.flatMapConcat { it.asFlow() }
        .onEach(_clues::tryEmit)
}
