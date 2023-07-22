package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.usecase.flowUseCase
import com.micrantha.skouter.domain.model.ColorClue
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.SegmentClue
import com.micrantha.skouter.domain.repository.ColorRepository
import com.micrantha.skouter.domain.repository.DetectionRepository
import com.micrantha.skouter.domain.repository.LabelRepository
import com.micrantha.skouter.domain.repository.SegmentRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScanError
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedColor
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedDetection
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedLabel
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedSegment
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class AnalyzeCaptureUseCase(
    private val labelRepository: LabelRepository,
    private val detectionRepository: DetectionRepository,
    private val segmentRepository: SegmentRepository,
    private val colorRepository: ColorRepository,
    private val dispatcher: Dispatcher
) : Dispatcher by dispatcher {

    operator fun invoke(image: CameraImage) = flowUseCase {
        labelRepository.labelAsync(image)
        detectionRepository.detectAsync(image)
        segmentRepository.segmentAsync(image)
        colorRepository.colorAsync(image)

        merge(
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
    }
}
