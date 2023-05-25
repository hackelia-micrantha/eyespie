package com.micrantha.skouter.data.player.mapping

import com.micrantha.bluebell.data.err.fail
import com.micrantha.skouter.data.local.mapping.DomainMapper
import com.micrantha.skouter.data.thing.model.ThingResponse
import com.micrantha.skouter.domain.models.Thing
import kotlinx.datetime.toInstant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class PlayerDomainMapper(
    private val mapper: DomainMapper
) {
    operator fun invoke(data: List<ThingResponse>) = data.map {
        Thing.Listing(
            id = it.id,
            name = it.name,
            createdAt = it.created_at.toInstant(),
            nodeId = it.id,
            guessed = it.guessed ?: false,
            image = it.image?.let { img ->
                mapper(Json.decodeFromJsonElement(img))
            } ?: fail("no image for thing ${it.id}")
        )
    }
}
