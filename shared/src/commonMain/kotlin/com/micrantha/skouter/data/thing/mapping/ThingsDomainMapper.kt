package com.micrantha.skouter.data.thing.mapping

import com.micrantha.skouter.PlayerNearbyThingsQuery
import com.micrantha.skouter.data.thing.model.RecognitionResponse
import com.micrantha.skouter.data.thing.model.ThingResponse
import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.domain.model.Player
import com.micrantha.skouter.domain.model.Thing
import kotlinx.datetime.toInstant

class ThingsDomainMapper {

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
        imageUrl = data.imageUrl
    )

    fun map(data: PlayerNearbyThingsQuery.Node) = Thing.Listing(
        id = data.id,
        nodeId = data.nodeId,
        createdAt = data.created_at.toInstant(),
        name = data.name,
        guessed = data.guessed ?: false,
        imageUrl = data.imageUrl
    )

    fun map(data: PlayerNearbyThingsQuery.Created_by) = Player.Ref(
        id = data.nodeId,
        name = data.name
    )

}
