package com.micrantha.skouter.ui

import com.micrantha.skouter.ui.games.create.GameCreateEnvironment
import com.micrantha.skouter.ui.games.create.GameCreateViewModel
import com.micrantha.skouter.ui.games.details.GameDetailsEnvironment
import com.micrantha.skouter.ui.games.details.GameDetailsViewModel
import com.micrantha.skouter.ui.games.list.GameListEnvironment
import com.micrantha.skouter.ui.games.list.GameListViewModel
import com.micrantha.skouter.ui.login.LoginEnvironment
import com.micrantha.skouter.ui.login.LoginViewModel
import org.koin.dsl.module

internal fun uiModules() = module {
    factory { GameListEnvironment(get(), get(), get(), get()) }

    factory { LoginEnvironment(get(), get(), get(), get()) }

    factory { MainEnvironment(get()) }

    factory { GameDetailsEnvironment(get(), get(), get(), get()) }

    factory { GameCreateEnvironment() }

    factory { GameListViewModel(get(), get()) }

    factory { GameDetailsViewModel(get(), get(), get()) }

    factory { MainViewModel(get(), get()) }

    factory { LoginViewModel(get(), get()) }

    factory { GameCreateViewModel(get()) }
}
