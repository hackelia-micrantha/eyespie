package com.micrantha.eyespie.ui.game.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.eyespie.domain.model.Game
import com.micrantha.eyespie.domain.model.Game.Limits
import com.micrantha.eyespie.ui.PreviewContext
import kotlinx.datetime.Clock.System
import kotlin.time.Duration

class GameDetailsProvider : PreviewParameterProvider<GameDetailsUiState> {

    override val values = sequenceOf(
        GameDetailsUiState(
            status = UiResult.Ready(
                Game(
                    id = "123",
                    name = "Game Preview",
                    createdAt = System.now(),
                    expires = System.now().plus(Duration.parse("8h")),
                    limits = Limits(
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
    GameDetailsScreen(GameDetailScreenArg("1234", "test"))
}
