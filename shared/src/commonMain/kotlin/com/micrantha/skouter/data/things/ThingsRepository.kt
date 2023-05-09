package com.micrantha.skouter.data.things

import com.micrantha.skouter.data.things.source.ThingsRemoteSource
import com.micrantha.skouter.domain.models.Clues
import com.micrantha.skouter.domain.models.Thing.Image
import com.micrantha.skouter.domain.repository.ThingsRepository as DomainRepository

class ThingsRepository(
    private val remoteSource: ThingsRemoteSource
) : DomainRepository {
    override suspend fun recognize(image: ByteArray, contentType: String): Result<Clues> =
        remoteSource.recognize(image, contentType)

    override suspend fun image(image: Image): Result<ByteArray> =
        remoteSource.image(image)

}
