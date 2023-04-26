package com.micrantha.skouter.ui

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.ui.MainAction.Refresh
import com.micrantha.skouter.ui.MainAction.SetTitle

typealias BackHandler = () -> Unit

sealed class MainAction : Action {
    data class SetTitle(val title: String, val showBack: Boolean = false) : MainAction()

    object Refresh : MainAction()

    data class CheckedLogin(val isLoggedIn: Boolean) : MainAction()

    companion object {
        fun setTitle(init: ScaffoldBuilder.() -> Unit) = ScaffoldBuilder().apply(init).asTitle()
    }
}

class ScaffoldBuilder {
    var title: String? = null
    var showBack: Boolean = false
    var onBack: BackHandler? = null

    fun asTitle() = title?.let { SetTitle(it, showBack) } ?: Refresh
}

data class MainState(
    val title: String? = null,
    val showBack: Boolean = false,
    val onBack: BackHandler? = null,
    val isLoggedIn: Boolean = false
)

class MainEnvironment(
    private val accountRepository: AccountRepository
) {
    suspend fun isLoggedIn() = accountRepository.isLoggedIn()
}
