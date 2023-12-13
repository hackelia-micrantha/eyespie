package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.skouter.domain.model.ColorClue
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.domain.repository.ColorRepository
import com.micrantha.skouter.domain.repository.DetectionRepository
import com.micrantha.skouter.domain.repository.LabelRepository
import com.micrantha.skouter.domain.repository.SegmentRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScanError
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedColor
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedDetection
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedLabel
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedSegment
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlin.coroutines.coroutineContext

class AnalyzeCaptureUseCase(
    private val labelRepository: LabelRepository,
    private val detectionRepository: DetectionRepository,
    private val segmentRepository: SegmentRepository,
    private val colorRepository: ColorRepository,
    private val dispatcher: Dispatcher
) : Dispatcher by dispatcher {

    val clues = merge(
        labelRepository.labels(),
        detectionRepository.detections(),
        segmentRepository.segments(),
        colorRepository.colors()
    ).map {
        when (it) {
            is LabelClue -> ScannedLabel(it)
            is ColorClue -> ScannedColor(it)
            is DetectClue -> ScannedDetection(it)
            is SegmentClue -> ScannedSegment(it)
            else -> ScanError
        }
    }

    suspend operator fun invoke(image: CameraImage): Result<Unit> =
        dispatchUseCase(coroutineContext) {
            labelRepository.analyze(image).onFailure { throw it }
            detectionRepository.analyze(image).onFailure { throw it }
            segmentRepository.analyze(image).onFailure { throw it }
            colorRepository.analyze(image).onFailure { throw it }
        }
}
