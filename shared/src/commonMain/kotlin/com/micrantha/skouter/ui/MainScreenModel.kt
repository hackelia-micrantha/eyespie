package com.micrantha.skouter.ui

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.navigate
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.ui.MainAction.Load
import com.micrantha.skouter.ui.MainAction.Loaded
import com.micrantha.skouter.ui.MainAction.Login
import com.micrantha.skouter.ui.dashboard.DashboardScreen
import com.micrantha.skouter.ui.login.LoginScreen
import kotlinx.coroutines.launch

class MainScreenModel(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
) : ScreenModel, Dispatcher, Router by context.router {

    override fun dispatch(action: Action) {
        coroutineScope.launch {
            invoke(action)
        }
    }

    suspend fun invoke(action: Action) {
        when (action) {
            is Load -> accountRepository.session().onFailure {
                dispatch(Login)
            }.onSuccess {
                dispatch(Loaded)
            }

            is Login -> navigate<LoginScreen>(Router.Options.Replace)
            is Loaded -> navigate<DashboardScreen>(Router.Options.Replace)
        }
    }
}
