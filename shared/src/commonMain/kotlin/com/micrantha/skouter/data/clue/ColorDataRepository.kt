package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.RepositoryStore
import com.micrantha.skouter.data.clue.source.ColorCaptureLocalSource
import com.micrantha.skouter.data.clue.source.ColorStreamLocalSource
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.repository.ColorRepository
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.model.ImageColors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.kodein.di.on

class ColorDataRepository(
    override val di: DI,
    private val captureSource: ColorCaptureLocalSource,
    private val mapper: ClueDomainMapper
) : DIAware, ColorRepository {
    private val store = RepositoryStore<ImageColors>()

    private val streamSource by on(this).instance<AnalyzerCallback<ImageColors>, ColorStreamLocalSource>(
        arg = AnalyzerCallback {
            store.update(it)
        })

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun colors() = store.value.flatMapConcat {
        it.map(mapper::color).asFlow()
    }

    override suspend fun color(image: CameraImage): Result<ColorProof> {
        return captureSource.analyze(image).onSuccess(store::update).map(mapper::color)
    }

    override fun colorAsync(image: CameraImage) {
        streamSource.analyze(image)
    }
}
