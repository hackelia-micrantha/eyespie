package com.micrantha.skouter.ui.game.list

import com.micrantha.bluebell.ui.screen.MappedScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext

class GameListScreenModel(
    context: ScreenContext,
    environment: GameListEnvironment,
    initialState: GameListState = GameListState()
) : MappedScreenModel<GameListState, GameListUiState>(
    context,
    initialState,
    environment::map
) {
    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
