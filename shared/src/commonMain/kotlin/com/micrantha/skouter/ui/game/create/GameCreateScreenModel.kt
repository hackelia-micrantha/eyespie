package com.micrantha.skouter.ui.game.create

import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.StatefulScreenModel

class GameCreateScreenModel(
    screenContext: ScreenContext,
    initialState: GameCreateState = GameCreateState()
) : StatefulScreenModel<GameCreateState>(
    context = screenContext,
    initialState = initialState
) {

}
