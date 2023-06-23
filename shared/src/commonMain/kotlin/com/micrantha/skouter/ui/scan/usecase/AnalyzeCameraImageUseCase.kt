package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.skouter.domain.repository.ClueRepository
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedColors
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedLabels
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedMatch
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedObjects
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedSegments
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class AnalyzeCameraImageUseCase(
    private val clueRepository: ClueRepository,
    private val dispatcher: Dispatcher
) : Dispatcher by dispatcher {

    suspend operator fun invoke(image: CameraImage) = dispatchUseCase {
        joinAll(
            launch {
                labels(image)
            },
            launch {
                detections(image)
            },
            launch {
                segments(image)
            },
            launch {
                colors(image)
            },
            launch {
                matches(image)
            }
        )
    }

    private suspend fun labels(image: CameraImage) =
        clueRepository.label(image).onSuccess { labels ->
            dispatch(ScannedLabels(labels))
        }.onFailure {
            Log.e("unable to scan labels", it)
        }

    private suspend fun colors(image: CameraImage) =
        clueRepository.color(image).onSuccess { colors ->
            dispatch(ScannedColors(colors))
        }.onFailure {
            Log.e("unable to scan colors", it)
        }

    private suspend fun detections(image: CameraImage) =
        clueRepository.detect(image).onSuccess { objects ->
            dispatch(ScannedObjects(objects))
        }.onFailure {
            Log.e("unable to scan detections", it)
        }

    private suspend fun segments(image: CameraImage) =
        clueRepository.segments(image).onSuccess { segments ->
            dispatch(ScannedSegments(segments))
        }.onFailure {
            Log.e("unable to scan segments", it)
        }

    private suspend fun matches(image: CameraImage) =
        clueRepository.match(image).onSuccess { matches ->
            dispatch(ScannedMatch(matches))
        }.onFailure {
            Log.e("unable to scan matches", it)
        }

}
