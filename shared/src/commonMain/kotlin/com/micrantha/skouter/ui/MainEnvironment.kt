package com.micrantha.skouter.ui

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.model.Ready
import com.micrantha.bluebell.domain.model.UiResult.Busy
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.get
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.ui.MainAction.Load
import com.micrantha.skouter.ui.MainAction.Loaded
import com.micrantha.skouter.ui.dashboard.DashboardScreen
import com.micrantha.skouter.ui.login.LoginScreen

class MainEnvironment(
    private val context: ScreenContext,
    private val accountRepository: AccountRepository,
    private val dispatcher: Dispatcher,
) : Reducer<MainState>, Effect<MainState>, Dispatcher by dispatcher {

    override fun reduce(state: MainState, action: Action) = when (action) {
        is Load -> state.copy(status = Busy("Initializing"))
        is Loaded -> state.copy(
            isLoggedIn = action.isLoggedIn,
            status = Ready()
        )
        else -> state
    }

    override suspend fun invoke(action: Action, state: MainState) {
        when (action) {
            is Load -> dispatch(
                Loaded(accountRepository.isLoggedIn())
            )
            is Loaded -> context.navigate(
                if (action.isLoggedIn)
                    context.get<DashboardScreen>()
                else
                    context.get<LoginScreen>()
            )
        }
    }
}
