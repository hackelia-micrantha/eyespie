package com.micrantha.eyespie.app.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.navigate
import com.micrantha.eyespie.app.ui.MainAction.Load
import com.micrantha.eyespie.domain.repository.AccountRepository
import com.micrantha.eyespie.features.login.ui.LoginScreen
import com.micrantha.eyespie.features.players.domain.usecase.LoadSessionPlayerUseCase
import kotlinx.coroutines.launch

class MainScreenModel(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
    private val loadSessionPlayerUseCase: LoadSessionPlayerUseCase
) : ScreenModel, Dispatcher, Router by context.router {

    override val dispatchScope = screenModelScope

    override fun dispatch(action: Action) {
        dispatchScope.launch {
            send(action)
        }
    }

    override suspend fun send(action: Action) {
        when (action) {
            is Load -> accountRepository.session().onFailure {
                context.navigate<LoginScreen>(Router.Options.Replace)
            }.onSuccess { session ->
                loadSessionPlayerUseCase.withNavigation(session)
            }
        }
    }
}
