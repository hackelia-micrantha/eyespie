package com.micrantha.eyespie.ui.game.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.eyespie.domain.model.Game
import com.micrantha.eyespie.ui.PreviewContext
import kotlinx.datetime.Clock.System
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

class GameListPreviewContent : PreviewParameterProvider<GameListUiState> {
    override val values = sequenceOf(
        GameListUiState(
            status = Ready(
                List(3) { id ->
                    Game.Listing(
                        id = "id$id",
                        nodeId = "n123$id",
                        name = "Preview Game $id",
                        createdAt = System.now().plus(id, DateTimeUnit.SECOND),
                        expiresAt = System.now().plus(id, DateTimeUnit.HOUR),
                        totalThings = 4,
                        totalPlayers = 6
                    )
                }
            )
        ),
        GameListUiState(
            status = UiResult.Default
        ),
        GameListUiState(
            status = UiResult.Busy("Loading...")
        ),
        GameListUiState(
            status = UiResult.Empty("No games found")
        ),
        GameListUiState(
            status = UiResult.Failure("Unable to load")
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
fun GameListPreview(@PreviewParameter(GameListPreviewContent::class) state: GameListUiState) =
    PreviewContext(state) {
        GameListScreen(it)
    }
