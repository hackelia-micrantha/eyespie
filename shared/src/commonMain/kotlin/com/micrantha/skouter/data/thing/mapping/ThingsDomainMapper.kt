package com.micrantha.skouter.data.thing.mapping

import com.micrantha.skouter.PlayerNearbyThingsQuery
import com.micrantha.skouter.data.thing.model.RecognitionResponse
import com.micrantha.skouter.data.thing.model.ThingListing
import com.micrantha.skouter.data.thing.model.ThingNearby
import com.micrantha.skouter.data.thing.model.ThingRequest
import com.micrantha.skouter.data.thing.model.ThingResponse
import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.domain.model.Player
import com.micrantha.skouter.domain.model.Thing
import kotlinx.datetime.Clock.System
import kotlinx.datetime.toInstant

class ThingsDomainMapper {

    fun recognition(data: RecognitionResponse): Clues {
        val label = data.labels.maxByOrNull { it.probability }
        return Clues(what = label?.label)
    }

    fun new(name: String, url: String, createdBy: String) = ThingRequest(
        name = name,
        imageUrl = url,
        created_by = createdBy
    )

    fun map(thing: Thing) = ThingRequest(
        id = thing.id,
        created_at = thing.createdAt.toString(),
        name = thing.name,
        imageUrl = thing.imageUrl,
        guessed = thing.guessed,
        created_by = thing.createdBy.id,
    )

    fun map(data: ThingResponse) = Thing(
        id = data.id!!,
        createdAt = data.created_at?.toInstant() ?: System.now(),
        name = data.name,
        imageUrl = data.imageUrl,
        guessed = data.guessed ?: false,
        createdBy = Player.Ref(
            id = data.created_by,
            name = "" // TODO: graphql
        ),
        guesses = emptyList(),
        nodeId = "",
        clues = Clues()
    )

    fun list(data: ThingListing) = Thing.Listing(
        id = data.id!!,
        name = data.name,
        createdAt = data.created_at?.toInstant() ?: System.now(),
        nodeId = data.id,
        guessed = data.guessed ?: false,
        imageUrl = data.imageUrl
    )

    fun nearby(data: ThingNearby) = Thing.Listing(
        id = data.id,
        nodeId = data.nodeId,
        createdAt = data.created_at.toInstant(),
        name = data.name,
        guessed = data.guessed ?: false,
        imageUrl = data.imageUrl
    )

    private fun createdBy(data: PlayerNearbyThingsQuery.Created_by) = Player.Ref(
        id = data.nodeId,
        name = data.name
    )

}
