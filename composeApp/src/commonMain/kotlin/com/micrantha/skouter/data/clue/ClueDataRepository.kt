package com.micrantha.skouter.data.clue

import com.micrantha.bluebell.data.Mapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.domain.repository.CameraRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.components.CaptureAnalyzer
import com.micrantha.skouter.platform.scan.components.StreamAnalyzer

abstract class ClueDataRepository<Source, Input, Output>(
    private val captureSource: Source,
    private val mapper: Mapper<Input, Output>
) : CameraRepository<Output> where Source : CaptureAnalyzer<Input>, Source : StreamAnalyzer<Input> {
    private val store = RepositoryStore<Input>()

    override suspend fun capture(image: CameraImage): Result<Output> {
        return captureSource.analyzeCapture(image).onSuccess(store::update).map(mapper::invoke)
    }

    override fun stream(image: CameraImage) {
        captureSource.analyzeStream(image, store::update)
    }
}