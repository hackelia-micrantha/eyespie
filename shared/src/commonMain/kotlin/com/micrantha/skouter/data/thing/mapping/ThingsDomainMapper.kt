package com.micrantha.skouter.data.thing.mapping

import com.micrantha.bluebell.data.err.fail
import com.micrantha.skouter.PlayerNearbyThingsQuery
import com.micrantha.skouter.data.local.mapping.DomainMapper
import com.micrantha.skouter.data.thing.model.RecognitionResponse
import com.micrantha.skouter.data.thing.model.ThingResponse
import com.micrantha.skouter.domain.models.Clues
import com.micrantha.skouter.domain.models.Player
import com.micrantha.skouter.domain.models.Thing
import kotlinx.datetime.toInstant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ThingsDomainMapper(
    private val mapper: DomainMapper
) {

    fun map(data: RecognitionResponse): Clues {
        val label = data.labels.maxByOrNull { it.probability }
        return Clues(what = label?.label)
    }

    fun map(data: ThingResponse) = Thing.Listing(
        id = data.id,
        name = data.name,
        createdAt = data.created_at.toInstant(),
        nodeId = data.id,
        guessed = data.guessed ?: false,
        image = data.image?.let { img ->
            mapper(Json.decodeFromJsonElement(img))
        } ?: fail("no image for thing ${data.id}")
    )

    fun map(data: PlayerNearbyThingsQuery.Node) = Thing.Listing(
        id = data.id,
        nodeId = data.nodeId,
        createdAt = data.created_at.toInstant(),
        name = data.name,
        guessed = data.guessed ?: false,
        image = data.image?.let { json ->
            mapper(Json.decodeFromString(json.toString()))
        } ?: fail("no image for thing(${data.id})"),
    )

    fun map(data: PlayerNearbyThingsQuery.Created_by) = Player.Ref(
        id = data.nodeId,
        name = data.name
    )

}
