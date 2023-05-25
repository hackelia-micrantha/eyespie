package com.micrantha.skouter.ui

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.ui.components.navigate
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.ui.MainAction.Load
import com.micrantha.skouter.ui.MainAction.Loaded
import com.micrantha.skouter.ui.MainAction.Login
import com.micrantha.skouter.ui.dashboard.DashboardScreen
import com.micrantha.skouter.ui.login.LoginScreen

class MainEnvironment(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
    private val dispatcher: Dispatcher,
) : Effect<Unit>, Dispatcher by dispatcher {

    override suspend fun invoke(action: Action, state: Unit) {
        when (action) {
            is Load -> accountRepository.account().onFailure {
                dispatch(Login)
            }.onSuccess {
                dispatch(Loaded)
            }
            is Login -> context.navigate<LoginScreen>()
            is Loaded -> context.navigate<DashboardScreen>()
        }
    }
}
