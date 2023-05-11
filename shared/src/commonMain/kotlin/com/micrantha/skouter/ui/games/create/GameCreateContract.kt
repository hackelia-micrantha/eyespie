package com.micrantha.skouter.ui.games.create

data class GameCreateState(
    val name: String = "",
    val expires: String? = null,
    val turnDuration: String? = null,
    val playerLimits: IntRange? = null,
    val thingLimits: IntRange? = null
)

class GameCreateEnvironment(
) {
}
