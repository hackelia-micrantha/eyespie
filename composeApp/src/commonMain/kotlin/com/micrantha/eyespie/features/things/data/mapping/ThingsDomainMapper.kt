package com.micrantha.eyespie.features.things.data.mapping

import com.micrantha.eyespie.core.data.system.mapping.LocationDomainMapper
import com.micrantha.eyespie.domain.entities.Clues
import com.micrantha.eyespie.domain.entities.ColorClue
import com.micrantha.eyespie.domain.entities.Embedding
import com.micrantha.eyespie.domain.entities.LabelClue
import com.micrantha.eyespie.domain.entities.Location.Point
import com.micrantha.eyespie.features.players.domain.entities.Player
import com.micrantha.eyespie.domain.entities.Proof
import com.micrantha.eyespie.domain.entities.Thing
import com.micrantha.eyespie.features.scan.data.mapping.ClueDomainMapper
import com.micrantha.eyespie.features.scan.data.model.LabelClueData
import com.micrantha.eyespie.features.scan.data.model.ProofData
import com.micrantha.eyespie.features.things.data.model.MatchRequest
import com.micrantha.eyespie.features.things.data.model.MatchResponse
import com.micrantha.eyespie.features.things.data.model.NearbyRequest
import com.micrantha.eyespie.features.things.data.model.ThingListing
import com.micrantha.eyespie.features.things.data.model.ThingRequest
import com.micrantha.eyespie.features.things.data.model.ThingResponse
import com.micrantha.eyespie.features.things.data.model.toImageEmbedding
import com.micrantha.eyespie.features.things.data.model.toJsonElement
import kotlin.time.Clock.System
import kotlin.time.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
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
            guessed = data.guessed == true,
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
        guessed = data.guessed == true,
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
