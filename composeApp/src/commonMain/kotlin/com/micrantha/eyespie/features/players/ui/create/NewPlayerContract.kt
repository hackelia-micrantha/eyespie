package com.micrantha.eyespie.features.players.ui.create

import com.micrantha.bluebell.ui.model.TextEntryState
import com.micrantha.bluebell.ui.model.UiResult

sealed interface Action {
    data class ChangedFirstName(val firstName: String) : Action
    data class ChangedLastName(val lastName: String) : Action
    data class ChangedNickName(val nickName: String) : Action
    data object OnSave : Action
    data class OnError(val error: Throwable) : Action
}

data class NewPlayerState(
    val firstName: String = "",
    val lastName: String = "",
    val nickName: String = "",
    val status: UiResult<Unit> = UiResult.Default
)

data class NewPlayerUiState(
    val firstName: TextEntryState = TextEntryState(""),
    val lastName: TextEntryState = TextEntryState(""),
    val nickName: TextEntryState = TextEntryState(""),
    val status: UiResult<Unit> = UiResult.Default
)