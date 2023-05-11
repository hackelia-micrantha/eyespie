package com.micrantha.skouter.ui

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.Ready
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.bluebell.domain.model.UiResult.Busy
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.ui.MainAction.Load
import com.micrantha.skouter.ui.MainAction.Loaded

sealed class MainAction : Action {

    data class Loaded(val isLoggedIn: Boolean) : MainAction()

    object Load : MainAction()

}

data class MainState(
    val isLoggedIn: Boolean = false,
    val status: UiResult<Unit> = UiResult.Default
)

class MainEnvironment(
    private val accountRepository: AccountRepository
) {
    suspend fun isLoggedIn() = accountRepository.isLoggedIn()

    fun reduce(state: MainState, action: Action) = when (action) {
        is Load -> state.copy(status = Busy())
        is Loaded -> state.copy(
            isLoggedIn = action.isLoggedIn,
            status = Ready()
        )
        else -> state
    }
}
