package com.micrantha.eyespie.data.thing.mapping

import com.micrantha.eyespie.data.clue.mapping.ClueDomainMapper
import com.micrantha.eyespie.data.clue.model.LabelClueData
import com.micrantha.eyespie.data.clue.model.ProofData
import com.micrantha.eyespie.data.system.mapping.LocationDomainMapper
import com.micrantha.eyespie.data.thing.model.MatchRequest
import com.micrantha.eyespie.data.thing.model.MatchResponse
import com.micrantha.eyespie.data.thing.model.NearbyRequest
import com.micrantha.eyespie.data.thing.model.ThingListing
import com.micrantha.eyespie.data.thing.model.ThingRequest
import com.micrantha.eyespie.data.thing.model.ThingResponse
import com.micrantha.eyespie.data.thing.model.toImageEmbedding
import com.micrantha.eyespie.data.thing.model.toJsonElement
import com.micrantha.eyespie.domain.model.Clues
import com.micrantha.eyespie.domain.model.ColorClue
import com.micrantha.eyespie.domain.model.Embedding
import com.micrantha.eyespie.domain.model.LabelClue
import com.micrantha.eyespie.domain.model.Location.Point
import com.micrantha.eyespie.domain.model.Player
import com.micrantha.eyespie.domain.model.Proof
import com.micrantha.eyespie.domain.model.Thing
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ThingsDomainMapper(
    private val locationMapper: LocationDomainMapper,
    private val clueMapper: ClueDomainMapper
) {

    fun new(proof: Proof) =
        ThingRequest(
            name = proof.name,
            imageUrl = proof.image.toString(),
            proof = proof.clues?.let { prove(it) },
            created_by = proof.playerID,
            location = proof.location.toString(),
            embedding = proof.match.toJsonElement()
        )

    fun map(thing: Thing) = ThingRequest(
        id = thing.id,
        created_at = thing.createdAt.toString(),
        name = thing.name,
        imageUrl = thing.imageUrl,
        guessed = thing.guessed,
        created_by = thing.createdBy.id,
        location = thing.location.toString(),
        embedding = thing.embedding.toJsonElement()
    )

    fun map(data: ThingResponse): Thing {
        val point = data.location?.let { locationMapper.point(it) } ?: Point()

        return Thing(
            id = data.id!!,
            createdAt = data.created_at?.let { Instant.parse(it) } ?: System.now(),
            name = data.name,
            imageUrl = data.imageUrl,
            guessed = data.guessed ?: false,
            createdBy = Player.Ref(
                id = data.created_by,
                name = "" // TODO: graphql
            ),
            guesses = emptyList(),
            location = point,
            clues = data.proof?.let { prove(it) } ?: Clues(),
            embedding = data.embedding.toImageEmbedding()
        )
    }

    fun list(data: ThingListing) = Thing.Listing(
        id = data.id!!,
        name = data.name,
        createdAt = data.created_at?.let { Instant.parse(it) } ?: System.now(),
        nodeId = data.id,
        guessed = data.guessed ?: false,
        imageUrl = data.imageUrl
    )

    fun nearby(location: Point, distance: Double) = NearbyRequest(
        latitude = location.latitude,
        longitude = location.longitude,
        distance = distance
    )

    fun match(embedding: Embedding) = MatchRequest(
        embedding = embedding.toJsonElement(),
        threshold = 0.5f,
        count = 5,
    )

    fun match(data: MatchResponse) = Thing.Match(
        id = data.id,
        image = Json.decodeFromJsonElement(data.content),
        similarity = data.similarity
    )

    private fun prove(data: Clues) = ProofData(
        labels = data.labels?.map { LabelClueData(it.data, it.confidence) },
        location = data.location?.data?.let { clueMapper.location(it) },
        colors = data.colors?.map { it.data }
    )

    private fun prove(data: ProofData) = Clues(
        labels = data.labels?.map { LabelClue(it.data, it.confidence) }?.toSet(),
        location = data.location?.let {
            clueMapper.clue(it)
        },
        colors = data.colors?.map { ColorClue(it) }?.toSet()
    )
}
