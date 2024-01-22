package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.domain.model.ColorClue
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.domain.repository.ColorRepository
import com.micrantha.skouter.domain.repository.DetectRepository
import com.micrantha.skouter.domain.repository.LabelRepository
import com.micrantha.skouter.domain.repository.SegmentRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.ui.scan.capture.ScanAction
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScanError
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedColor
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedDetection
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedLabel
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedSegment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AnalyzeCaptureUseCase(
    private val labelRepository: LabelRepository,
    private val detectionRepository: DetectRepository,
    private val segmentRepository: SegmentRepository,
    private val colorRepository: ColorRepository,
    private val dispatcher: Dispatcher
) : Dispatcher by dispatcher {

    private val _clues = MutableSharedFlow<ScanAction>(replay = 4)

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
    }.flatMapConcat { it.asFlow() }.map(::asScanState)
        .onEach(_clues::tryEmit)

    private fun asScanState(clue: Clue<*>) = when (clue) {
        is LabelClue -> ScannedLabel(clue)
        is ColorClue -> ScannedColor(clue)
        is DetectClue -> ScannedDetection(clue)
        is SegmentClue -> ScannedSegment(clue)
        else -> ScanError
    }
}
