package com.micrantha.skouter.data.local.mapping

import com.micrantha.skouter.data.thing.model.ImageJson
import com.micrantha.skouter.domain.model.Image

class DomainMapper {

    operator fun invoke(data: ImageJson) = Image(
        fileId = data.fileId,
        bucketId = data.bucketId,
        playerId = data.playerId
    )
}
