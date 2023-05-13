package com.micrantha.skouter.ui.components

import com.micrantha.bluebell.domain.i18n.LocalizedString

typealias S = Strings

enum class Strings(
    override val key: String,
    override val iosKey: String? = null,
    override val androidKey: String? = null
) : LocalizedString {
    LoadingGames("loading_games"),
    ResultStatusBusy("model_status_busy"),
    ResultStatusFailure("model_status_failure"),
    Games("games"),
    NetworkFailure("network_failure"),
    Login("login"),
    AppTitle("app_title"),
    LoginEmailPlaceholder("login_email_placeholder"),
    LoginPasswordPlaceholder("login_password_placeholder"),
    Email("email"),
    Password("password"),
    LoggingIn("logging_in"),
    LoginFailed("login_failed"),
    Things("things"),
    Players("players"),
    CreatedAt("created_at"),
    ExpiresAt("expires_at"),
    LoadingGame("loading_game"),
    NextTurn("next_turn"),
    Location("location"),
    NewGameHeader("new_game_header"),
    NewGameContent("new_game_content"),
    Name("name"),
    Next("next"),
    NewThing("new_thing"),
    NewThingDescription("new_thing_description"),
    LoadingDashboard("loading_dashboard")
}
