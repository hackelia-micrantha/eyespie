package com.micrantha.skouter.data.remote

import com.micrantha.skouter.GameListQuery
import com.micrantha.skouter.GetAccountQuery
import com.micrantha.skouter.domain.models.Clues
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Game.Limits
import com.micrantha.skouter.domain.models.Location
import com.micrantha.skouter.domain.models.Player
import com.micrantha.skouter.domain.models.Thing
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

class ApiMapper {

    operator fun invoke(data: GetAccountQuery.AccountGet) = Player(
        id = data._id,
        email = data.email,
        name = Player.Name(
            first = data.prefs?.firstName ?: "",
            last = data.prefs?.lastName ?: "",
            nick = data.name ?: ""
        ),
        score = Player.Score(
            total = data.prefs?.totalScore ?: 0
        )
    )

    operator fun invoke(data: GameListQuery.Game) =
        Game(
            id = data._id,
            name = data.name,
            createdAt = LocalDateTime.parse(data._createdAt),
            expires = LocalDateTime.parse(data.expires),
            limits = invoke(data.limits),
            turnDuration = Duration.parse(data.turnDuration),
            things = data.things?.filterNotNull()?.map { invoke(it) } ?: emptyList(),
            players = emptyList()
        )

    operator fun invoke(data: GameListQuery.Limits?) = Limits(
        player = IntRange(data?.player?.min ?: 1, data?.player?.max ?: 10),
        thing = IntRange(data?.thing?.min ?: 1, data?.thing?.max ?: 10)
    )

    operator fun invoke(data: GameListQuery.Thing1) =
        Thing(
            id = data._id,
            name = data.name!!,
            guesses = data.guesses?.filterNotNull()?.map { invoke(it) } ?: emptyList(),
            image = invoke(data.image!!),
            clues = invoke(data.clues)
        )

    operator fun invoke(data: GameListQuery.Guess) =
        Thing.Guess(
            at = LocalDateTime.parse(data.at!!),
            by = Player.Ref(data.by!!._id, data.by.name),
            correct = data.correct ?: false
        )

    operator fun invoke(data: GameListQuery.Image) = Thing.Image(
        fileId = data._id,
        bucketId = data.bucketId
    )

    operator fun invoke(clues: GameListQuery.Clues?) = clues?.let { data ->
        Clues(
            color = data.color,
            what = data.what,
            where = invoke(data.where),
            why = data.why,
            who = data.who,
            rhyme = data.rhyme
        )
    } ?: Clues()

    operator fun invoke(data: GameListQuery.Where?) = data?.let { loc ->
        Location(
            latitude = loc.latitude,
            longitude = loc.longitude,
            name = loc.name,
            city = loc.city,
            region = loc.region,
            country = loc.country,
            accuracy = loc.accuracy
        )
    } ?: Location()
}
