package com.micrantha.eyespie.features.scan.data

import com.micrantha.eyespie.features.scan.data.source.ColorCaptureLocalSource
import com.micrantha.eyespie.domain.entities.ColorProof
import com.micrantha.eyespie.domain.repository.ColorRepository
import com.micrantha.eyespie.platform.scan.CameraImage
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
