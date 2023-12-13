package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.data.clue.source.ColorCaptureLocalSource
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.repository.ColorRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.model.ImageColors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware

class ColorDataRepository(
    override val di: DI,
    private val captureSource: ColorCaptureLocalSource,
    private val mapper: ClueDomainMapper
) : DIAware, ColorRepository {
    private val store = RepositoryStore<ImageColors>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun colors() = store.value.flatMapConcat {
        it.map(mapper::color).asFlow()
    }

    override suspend fun analyze(image: CameraImage): Result<ColorProof> {
        return captureSource.analyze(image).onSuccess(store::update).map(mapper::color)
    }
}
