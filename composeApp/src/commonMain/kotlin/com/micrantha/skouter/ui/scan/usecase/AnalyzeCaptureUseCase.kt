package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.skouter.domain.model.ColorClue
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.domain.repository.ColorRepository
import com.micrantha.skouter.domain.repository.DetectionRepository
import com.micrantha.skouter.domain.repository.LabelRepository
import com.micrantha.skouter.domain.repository.SegmentRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.ui.scan.capture.ScanAction
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScanError
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedColor
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedDetection
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedLabel
import com.micrantha.skouter.ui.scan.capture.ScanAction.ScannedSegment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class AnalyzeCaptureUseCase(
    private val labelRepository: LabelRepository,
    private val detectionRepository: DetectionRepository,
    private val segmentRepository: SegmentRepository,
    private val colorRepository: ColorRepository,
    private val dispatcher: Dispatcher
) : Dispatcher by dispatcher {

    private val clues = merge(
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

    operator fun invoke(image: CameraImage): Result<Flow<ScanAction>> = try {
        labelRepository.stream(image)
        detectionRepository.stream(image)
        segmentRepository.stream(image)
        colorRepository.stream(image)
        Result.success(clues)
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
