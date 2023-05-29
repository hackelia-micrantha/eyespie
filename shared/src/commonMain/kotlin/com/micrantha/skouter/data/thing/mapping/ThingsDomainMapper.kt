package com.micrantha.skouter.data.thing.mapping

import com.micrantha.skouter.data.clue.model.LabelClueData
import com.micrantha.skouter.data.clue.model.ProofData
import com.micrantha.skouter.data.thing.model.NearbyRequest
import com.micrantha.skouter.data.thing.model.ThingListing
import com.micrantha.skouter.data.thing.model.ThingRequest
import com.micrantha.skouter.data.thing.model.ThingResponse
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.Player
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.domain.model.Thing
import kotlinx.datetime.Clock.System
import kotlinx.datetime.toInstant

class ThingsDomainMapper {

    fun new(name: String, url: String, proof: Proof, createdBy: String) = ThingRequest(
        name = name,
        imageUrl = url,
        proof = proof(proof),
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
        clues = data.proof?.let { proof(it) } ?: Proof()
    )

    fun list(data: ThingListing) = Thing.Listing(
        id = data.id!!,
        name = data.name,
        createdAt = data.created_at?.toInstant() ?: System.now(),
        nodeId = data.id,
        guessed = data.guessed ?: false,
        imageUrl = data.imageUrl
    )

    fun nearby(location: Location.Point, distance: Double) = NearbyRequest(
        latitude = location.latitude,
        longitude = location.longitude,
        distance = distance
    )

    private fun proof(data: Proof) = ProofData(
        labels = data.labels?.map { LabelClueData(it.data, it.confidence) }
    )

    private fun proof(data: ProofData) = Proof(
        labels = data.labels?.map { LabelClue(it.data, it.confidence) }
    )
}
