package com.micrantha.eyespie.ui.game.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.eyespie.domain.entities.Game
import com.micrantha.eyespie.features.game.ui.detail.GameDetailScreenArg
import com.micrantha.eyespie.features.game.ui.detail.GameDetailsScreen
import com.micrantha.eyespie.features.game.ui.detail.GameDetailsUiState
import com.micrantha.eyespie.ui.PreviewContext
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class GameDetailsProvider : PreviewParameterProvider<GameDetailsUiState> {

    @OptIn(ExperimentalTime::class)
    override val values = sequenceOf(
        GameDetailsUiState(
            status = UiResult.Ready(
                Game(
                    id = "123",
                    name = "Game Preview",
                    createdAt = Clock.System.now(),
                    expires = Clock.System.now().plus(Duration.parse("8h")),
                    limits = Game.Limits(
                        player = IntRange.EMPTY,
                        thing = IntRange.EMPTY,
                    ),
                    players = emptyList(),
                    things = emptyList(),
                    turnDuration = Duration.parse("8h")
                ),
            ),
        )
    )
}

@Preview
@Composable
fun GameDetailsPreview(
    @PreviewParameter(GameDetailsProvider::class) state: GameDetailsUiState
) = PreviewContext(state) {
    GameDetailsScreen(it,GameDetailScreenArg("1234", "test"))
}
