package com.micrantha.skouter.ui.game.list

import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenMappedModel

class GameListScreenModel(
    viewContext: ScreenContext,
    environment: GameListEnvironment,
    initialState: GameListState = GameListState()
) : ScreenMappedModel<GameListState, GameListUiState>(
    viewContext,
    initialState,
    environment::map
) {
    init {
        store.addReducer(environment::reduce)
            .applyEffect(environment::invoke)
    }
}
