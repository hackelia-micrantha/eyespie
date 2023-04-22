package com.micrantha.skouter.ui.arch

import com.micrantha.bluebell.domain.i18n.LocalizedString

enum class i18n(
    override val key: String,
    override val iosKey: String? = null,
    override val androidKey: String? = null
) : LocalizedString {
    LoadingGames("loading_games"),
    ModelStatusBusy("model_status_busy"),
    ModelStatusFailure("model_status_failure"),
    GamesTitle("games_title"),
    NetworkFailure("network_failure")
}
