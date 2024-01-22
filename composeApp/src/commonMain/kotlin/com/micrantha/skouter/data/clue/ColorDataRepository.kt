package com.micrantha.skouter.data.clue

import com.micrantha.skouter.data.clue.source.ColorCaptureLocalSource
import com.micrantha.skouter.domain.model.ColorProof
import com.micrantha.skouter.domain.repository.ColorRepository
import com.micrantha.skouter.platform.scan.CameraImage
import org.kodein.di.DI
import org.kodein.di.DIAware

class ColorDataRepository(
    override val di: DI,
    private val localSource: ColorCaptureLocalSource,
) : DIAware, ColorRepository {

    override suspend fun analyze(image: CameraImage): Result<ColorProof> {
        return localSource.analyze(image)
    }
}
