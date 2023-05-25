package com.micrantha.skouter.data.thing.mapping

import com.micrantha.bluebell.data.err.fail
import com.micrantha.skouter.PlayerNearbyThingsQuery
import com.micrantha.skouter.data.local.mapping.DomainMapper
import com.micrantha.skouter.data.thing.model.RecognitionResponse
import com.micrantha.skouter.domain.models.Clues
import com.micrantha.skouter.domain.models.Player
import com.micrantha.skouter.domain.models.Thing
import com.micrantha.skouter.domain.models.ThingList
import kotlinx.datetime.toInstant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ThingsDomainMapper(
    private val mapper: DomainMapper
) {

    operator fun invoke(data: RecognitionResponse): Clues {
        val label = data.labels.maxByOrNull { it.probability }
        return Clues(what = label?.label)
    }

    operator fun invoke(data: List<PlayerNearbyThingsQuery.Edge>?): ThingList = data?.map {
        it.node
    }?.map {
        Thing.Listing(
            id = it.id,
            nodeId = it.nodeId,
            createdAt = it.created_at.toInstant(),
            name = it.name,
            guessed = it.guessed ?: false,
            image = it.image?.let { json ->
                mapper(Json.decodeFromString(json.toString()))
            } ?: fail("no image for thing(${it.id})"),
        )
    } ?: emptyList()

    private fun map(data: PlayerNearbyThingsQuery.Created_by) = Player.Ref(
        id = data.nodeId,
        name = data.name
    )

}
