package com.micrantha.eyespie.features.game.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.eyespie.domain.entities.Game
import com.micrantha.eyespie.ui.PreviewContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class GameListPreviewContent : PreviewParameterProvider<GameListUiState> {
    @OptIn(ExperimentalTime::class)
    override val values = sequenceOf(
        GameListUiState(
            status = UiResult.Ready(
                List(3) { id ->
                    Game.Listing(
                        id = "id$id",
                        nodeId = "n123$id",
                        name = "Preview Game $id",
                        createdAt = Clock.System.now().plus(id, DateTimeUnit.SECOND),
                        expiresAt = Clock.System.now().plus(id, DateTimeUnit.HOUR),
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
            status = UiResult.Busy()
        ),
        GameListUiState(
            status = UiResult.Empty()
        ),
        GameListUiState(
            status = UiResult.Failure()
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
fun GameListPreview(@PreviewParameter(GameListPreviewContent::class) state: GameListUiState) =
    PreviewContext(state) {
        GameListScreen(it)
    }
