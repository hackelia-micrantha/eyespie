package com.micrantha.eyespie.features.game.ui.create

import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.StatefulScreenModel

class GameCreateScreenModel(
    screenContext: ScreenContext,
    initialState: GameCreateState = GameCreateState()
) : StatefulScreenModel<GameCreateState>(
    context = screenContext,
    initialState = initialState
)
