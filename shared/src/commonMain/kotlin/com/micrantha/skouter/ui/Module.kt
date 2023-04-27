package com.micrantha.skouter.ui

import com.micrantha.skouter.ui.games.GameListEnvironment
import com.micrantha.skouter.ui.games.GameListViewModel
import com.micrantha.skouter.ui.login.LoginEnvironment
import com.micrantha.skouter.ui.login.LoginViewModel
import org.koin.dsl.module

internal fun uiModules() = module {
    factory { GameListEnvironment(get(), get()) }

    factory { LoginEnvironment(get()) }

    factory { MainEnvironment(get()) }

    factory { params -> GameListViewModel(params.get(), get()) }

    factory { params -> MainViewModel(params.get(), get()) }

    factory { params -> LoginViewModel(params.get(), get()) }
}
