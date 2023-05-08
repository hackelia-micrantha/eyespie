package com.micrantha.skouter.ui.games.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.micrantha.bluebell.domain.model.ResultStatus
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Game.Limits
import com.micrantha.skouter.ui.PreviewContext
import kotlinx.datetime.Clock.System
import kotlin.ranges.IntRange.Companion
import kotlin.time.Duration

@Preview
@Composable
fun GameDetailsPreview() = PreviewContext {
    GameDetailsContent(
        state = GameDetailsUiState(
            status = ResultStatus.Ready(
                Game(
                    id = "123",
                    nodeId = "123",
                    name = "Game Preview",
                    createdAt = System.now(),
                    expires = System.now().plus(Duration.parse("8h")),
                    limits = Limits(
                        player = IntRange.EMPTY,
                        thing = Companion.EMPTY,
                    ),
                    players = emptyList(),
                    things = emptyList(),
                    turnDuration = Duration.parse("8h")
                )
            )
        )
    ) {}

}
