package com.micrantha.skouter.data.thing.mapping

import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.model.LabelClueData
import com.micrantha.skouter.data.clue.model.ProofData
import com.micrantha.skouter.data.system.mapping.LocationDomainMapper
import com.micrantha.skouter.data.thing.model.NearbyRequest
import com.micrantha.skouter.data.thing.model.ThingListing
import com.micrantha.skouter.data.thing.model.ThingRequest
import com.micrantha.skouter.data.thing.model.ThingResponse
import com.micrantha.skouter.domain.model.LabelClue
import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.Location.Point
import com.micrantha.skouter.domain.model.Player
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.domain.model.Thing
import kotlinx.datetime.Clock.System
import kotlinx.datetime.toInstant

class ThingsDomainMapper(
    private val locationMapper: LocationDomainMapper,
    private val clueMapper: ClueDomainMapper
) {

    fun new(thing: Thing.Create, url: String, playerID: String, location: Location) =
        ThingRequest(
            name = thing.name,
            imageUrl = url,
            proof = proof(thing.proof, location),
            created_by = playerID,
            location = location.point.toString()
        )

    fun map(thing: Thing) = ThingRequest(
        id = thing.id,
        created_at = thing.createdAt.toString(),
        name = thing.name,
        imageUrl = thing.imageUrl,
        guessed = thing.guessed,
        created_by = thing.createdBy.id,
        location = thing.location.toString()
    )

    fun map(data: ThingResponse): Thing {
        val point = data.location?.let { locationMapper.point(it) } ?: Point()

        return Thing(
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
            nodeId = "", // TODO
            location = point,
            clues = prove(point, data.proof)
        )
    }

    fun list(data: ThingListing) = Thing.Listing(
        id = data.id!!,
        name = data.name,
        createdAt = data.created_at?.toInstant() ?: System.now(),
        nodeId = data.id,
        guessed = data.guessed ?: false,
        imageUrl = data.imageUrl
    )

    fun nearby(location: Point, distance: Double) = NearbyRequest(
        latitude = location.latitude,
        longitude = location.longitude,
        distance = distance
    )

    private fun prove(point: Point, data: ProofData?): Proof {
        val proof = data?.let { prove(it) } ?: Proof()
        return proof.update(point)
    }

    private fun proof(data: Proof, location: Location) = ProofData(
        labels = data.labels?.map { LabelClueData(it.data, it.confidence) },
        location = clueMapper.location(location)
    )

    private fun prove(data: ProofData) = Proof(
        labels = data.labels?.map { LabelClue(it.data, it.confidence) }?.toSet(),
        location = data.location?.let {
            clueMapper.clue(it)
        }
    )

    private fun Proof.update(point: Point): Proof = copy(
        location = location?.copy(data = location.data.copy(point = point))
    )
}
