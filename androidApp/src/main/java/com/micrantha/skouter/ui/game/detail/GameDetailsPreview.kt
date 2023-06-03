package com.micrantha.skouter.ui.game.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.micrantha.bluebell.domain.model.Ready
import com.micrantha.skouter.domain.model.Game
import com.micrantha.skouter.domain.model.Game.Limits
import com.micrantha.skouter.ui.PreviewContext
import kotlinx.datetime.Clock.System
import org.kodein.di.bind
import org.kodein.di.bindProvider
import org.kodein.di.factory
import org.kodein.di.instance
import kotlin.time.Duration

class GameDetailsProvider : PreviewParameterProvider<GameDetailsState> {

    override val values = sequenceOf(
        GameDetailsState(
            status = Ready(),
            game = Game(
                id = "123",
                nodeId = "123",
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
}

@Preview
@Composable
fun GameDetailsPreview(
    @PreviewParameter(GameDetailsProvider::class) state: GameDetailsState
) = PreviewContext(
    bindings = {
        bindProvider {
            GameDetailsScreenModel(
                    instance(),
                    instance(),
                    state
                )
        }
    }
) {
    GameDetailsScreen(GameDetailScreenArg("1234", "test")).Content()
}
