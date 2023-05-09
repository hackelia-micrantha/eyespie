package com.micrantha.skouter.ui.games.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.skouter.domain.models.GameListing
import com.micrantha.skouter.ui.PreviewContext
import kotlinx.datetime.Clock.System
import kotlin.time.Duration

@Preview
@Composable
fun GameListPreview() = PreviewContext {
    GameListContent(
        state = GameListUiState(
            status = Ready(
                listOf(
                    GameListing(
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
    ) {}
}
