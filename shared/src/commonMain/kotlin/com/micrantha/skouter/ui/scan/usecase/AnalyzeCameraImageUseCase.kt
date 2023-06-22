package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.usecase.flowUseCase
import com.micrantha.skouter.domain.repository.ClueRepository
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageScanned
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class AnalyzeCameraImageUseCase(
    private val clueRepository: ClueRepository
) {

    suspend operator fun invoke(image: CameraImage) = flowUseCase {
        combine(
            flow = labels(image),
            flow2 = colors(image),
            flow3 = detections(image),
            flow4 = segments(image),
            flow5 = matches(image)
        ) { label, color, detection, segment, match ->
            Result.success(ImageScanned(label, color, detection, segment, match))
        }
    }

    private fun labels(image: CameraImage) = flow {
        clueRepository.label(image).onSuccess { labels ->
            emitAll(labels.asFlow())
        }.onFailure {
            Log.e("unable to flow labels", it)
            throw it
        }
    }

    private fun colors(image: CameraImage) = flow {
        clueRepository.color(image).onSuccess { colors ->
            emitAll(colors.asFlow())
        }.onFailure {
            Log.e("unable to flow colors", it)
            throw it
        }
    }

    private fun detections(image: CameraImage) = flow {
        clueRepository.detect(image).onSuccess { detection ->
            emitAll(detection.asFlow())
        }.onFailure {
            Log.e("unable to flow detections", it)
            throw it
        }
    }

    private fun segments(image: CameraImage) = flow {
        clueRepository.segments(image).onSuccess { segment ->
            emitAll(segment.asFlow())
        }.onFailure {
            Log.e("unable to flow segments", it)
            throw it
        }
    }

    private fun matches(image: CameraImage) = flow {
        clueRepository.match(image).onSuccess { match ->
            emitAll(match.asFlow())
        }.onFailure {
            Log.e("unable to flow matches", it)
            throw it
        }
    }

}
