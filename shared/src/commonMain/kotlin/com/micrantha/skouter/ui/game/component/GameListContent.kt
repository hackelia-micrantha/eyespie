package com.micrantha.skouter.ui.game.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.skouter.domain.model.Game
import com.micrantha.skouter.ui.component.S
import com.micrantha.skouter.ui.game.list.GameListAction.NewGame

@Composable
fun GameListContent(games: List<Game.Listing>, dispatch: Dispatch) {
    when {
        games.isEmpty() -> EmptyContent(
            message = stringResource(S.NoGamesFound),
            icon = Icons.Default.SmartToy
        ) {
            dispatch(NewGame)
        }
        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(games) { game ->
                GameListCard(game, dispatch)
            }
        }
    }
}
