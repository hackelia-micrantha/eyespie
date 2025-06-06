package com.micrantha.eyespie.app.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.arch.Dispatcher
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.navigate
import com.micrantha.eyespie.app.ui.MainAction.Load
import com.micrantha.eyespie.app.ui.MainAction.Loaded
import com.micrantha.eyespie.app.ui.MainAction.Login
import com.micrantha.eyespie.domain.repository.AccountRepository
import com.micrantha.eyespie.features.dashboard.ui.DashboardScreen
import com.micrantha.eyespie.ui.login.LoginScreen
import kotlinx.coroutines.launch

class MainScreenModel(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
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
                dispatch(Login)
            }.onSuccess {
                dispatch(Loaded)
            }

            is Login -> context.navigate<LoginScreen>(Router.Options.Replace)
            is Loaded -> context.navigate<DashboardScreen>(Router.Options.Replace)
        }
    }
}
