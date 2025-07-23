package com.micrantha.eyespie.features.players.ui.create

import com.micrantha.bluebell.arch.Action as FluxAction
import com.micrantha.bluebell.arch.Effect
import com.micrantha.bluebell.arch.Reducer
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.arch.StateMapper
import com.micrantha.bluebell.domain.repository.LocalizedRepository
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.model.TextEntryState
import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.bluebell.ui.screen.navigate
import com.micrantha.eyespie.app.S
import com.micrantha.eyespie.core.data.account.model.CurrentSession
import com.micrantha.eyespie.features.dashboard.ui.DashboardScreen
import com.micrantha.eyespie.features.players.domain.repository.PlayerRepository
import eyespie.composeapp.generated.resources.save_failed

class NewPlayerEnvironment(
    private val context: ScreenContext,
    private val playerRepository: PlayerRepository,
    private val currentSession: CurrentSession
) : Reducer<NewPlayerState>, Effect<NewPlayerState>,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n,
    Router by context.router {

    companion object : StateMapper<NewPlayerState, NewPlayerUiState> {
        private var uiState: NewPlayerUiState? = null

        override fun map(state: NewPlayerState): NewPlayerUiState = uiState?.let {
            it.copy(
                firstName = it.firstName.copy(value = state.firstName),
                lastName = it.lastName.copy(value = state.lastName),
                nickName = it.nickName.copy(value = state.nickName)
            )
        } ?: NewPlayerUiState(
            firstName = TextEntryState(value = state.firstName),
            lastName = TextEntryState(value = state.lastName),
            nickName = TextEntryState(value = state.nickName)
        ).also {
            uiState = it
        }
    }

    override fun reduce(
        state: NewPlayerState,
        action: FluxAction
    ) = when(action) {
        is Action.ChangedFirstName -> state.copy(firstName = action.firstName)
        is Action.ChangedLastName -> state.copy(lastName = action.lastName)
        is Action.ChangedNickName -> state.copy(nickName = action.nickName)
        is Action.OnError -> state.copy(status = UiResult.Failure(S.save_failed))
        else -> state
    }

    fun map(state: NewPlayerState) = NewPlayerEnvironment.map(state)

    override suspend fun invoke(
        action: FluxAction,
        state: NewPlayerState
    ) {
        when (action) {
            is Action.OnSave -> playerRepository.create(
                currentSession.requireUserId(),
                state.firstName,
                state.lastName,
                state.nickName
            ).onSuccess {
                context.navigate<DashboardScreen>(Router.Options.Replace)
            }.onFailure {
                dispatch(Action.OnError(it))
            }
            else -> Unit
        }
    }
}