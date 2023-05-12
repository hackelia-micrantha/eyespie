package com.micrantha.skouter.ui.games.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.models.Game
import com.micrantha.skouter.domain.models.Game.Limits
import com.micrantha.skouter.ui.PreviewContext
import kotlinx.datetime.Clock.System
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import kotlin.time.Duration

class GameDetailsProvider : PreviewParameterProvider<GameDetailsState> {

    override val values = sequenceOf(
        GameDetailsState(
            status = UiResult.Ready(
                Game(
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
                )
            ),
            images = emptyMap()
        )
    )
}

@Preview
@Composable
fun GameDetailsPreview(
    @PreviewParameter(GameDetailsProvider::class) state: GameDetailsState
) = PreviewContext(
    bindings = {
        bind {
            factory { arg: GameDetailScreenArg ->
                GameDetailsScreenModel(
                    arg,
                    instance(),
                    instance(),
                    state
                )
            }
        }
    }
) {
    GameDetailsScreen(GameDetailScreenArg("1234", "test")).Content()
}
