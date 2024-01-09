package com.micrantha.skouter.data.clue

import com.micrantha.bluebell.data.MemoryStore
import com.micrantha.skouter.data.clue.source.ColorCaptureLocalSource
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.repository.ColorRepository
import com.micrantha.skouter.platform.scan.CameraImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware

class ColorDataRepository(
    override val di: DI,
    private val localSource: ColorCaptureLocalSource,
) : DIAware, ColorRepository {
    private val store = MemoryStore<ColorProof>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun results() = store.value.flatMapConcat { it.asFlow() }

    override suspend fun analyze(image: CameraImage): Result<ColorProof> {
        return localSource.analyze(image).onSuccess(store::update)
    }
}
