package com.micrantha.skouter.ui.game.create

import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.ScreenStatefulModel

class GameCreateScreenModel(
    screenContext: ScreenContext,
    initialState: GameCreateState = GameCreateState()
) : ScreenStatefulModel<GameCreateState>(
    context = screenContext,
    initialState = initialState
) {

}
