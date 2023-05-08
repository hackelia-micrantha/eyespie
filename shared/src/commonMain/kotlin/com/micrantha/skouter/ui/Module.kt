package com.micrantha.skouter.ui

import com.micrantha.skouter.ui.games.details.GameDetailsEnvironment
import com.micrantha.skouter.ui.games.details.GameDetailsViewModel
import com.micrantha.skouter.ui.games.list.GameListEnvironment
import com.micrantha.skouter.ui.games.list.GameListViewModel
import com.micrantha.skouter.ui.login.LoginEnvironment
import com.micrantha.skouter.ui.login.LoginViewModel
import org.koin.dsl.module

internal fun uiModules() = module {
    factory { GameListEnvironment(get()) }

    factory { LoginEnvironment(get()) }

    factory { MainEnvironment(get()) }

    factory { GameDetailsEnvironment(get(), get(), get(), get()) }

    factory { params -> GameListViewModel(params.get(), get()) }

    factory { params -> GameDetailsViewModel(params.get(), params.get(), get()) }

    factory { params -> MainViewModel(params.get(), get()) }

    factory { params -> LoginViewModel(params.get(), get()) }
}
