package com.micrantha.skouter.ui.game.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.skouter.domain.model.Game
import com.micrantha.skouter.ui.PreviewContext
import kotlinx.datetime.Clock.System
import kotlin.time.Duration

class GameListPreviewContent : PreviewParameterProvider<GameListUiState> {
    override val values = sequenceOf(
        GameListUiState(
            status = Ready(
                listOf(
                    Game.Listing(
                        id = "id",
                        nodeId = "n123",
                        name = "Preview Game",
                        createdAt = System.now(),
                        expiresAt = System.now().plus(Duration.parse("2d")),
                        totalThings = 4,
                        totalPlayers = 6
                    )
                )
            )
        )
    )
}

@Preview
@Composable
fun GameListPreview(@PreviewParameter(GameListPreviewContent::class) state: GameListUiState) =
    PreviewContext(state) {
        GameListScreen(it)
    }
