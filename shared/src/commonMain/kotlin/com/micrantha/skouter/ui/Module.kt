package com.micrantha.skouter.ui

import com.micrantha.skouter.ui.games.GameListEnvironment
import com.micrantha.skouter.ui.games.GameListViewModel
import org.koin.dsl.module

internal fun uiModules() = module {
    factory { GameListEnvironment(get(), get()) }

    factory { MainEnvironment(get()) }

    factory { params -> GameListViewModel(params.get(), get()) }

    factory { params -> MainViewModel(params.get(), get()) }
}
