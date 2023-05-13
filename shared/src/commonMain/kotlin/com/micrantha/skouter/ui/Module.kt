package com.micrantha.skouter.ui

import com.micrantha.skouter.ui.dashboard.DashboardEnvironment
import com.micrantha.skouter.ui.dashboard.DashboardScreen
import com.micrantha.skouter.ui.dashboard.DashboardScreenModel
import com.micrantha.skouter.ui.games.create.GameCreateScreen
import com.micrantha.skouter.ui.games.create.GameCreateScreenModel
import com.micrantha.skouter.ui.games.details.GameDetailScreenArg
import com.micrantha.skouter.ui.games.details.GameDetailsEnvironment
import com.micrantha.skouter.ui.games.details.GameDetailsScreen
import com.micrantha.skouter.ui.games.details.GameDetailsScreenModel
import com.micrantha.skouter.ui.games.list.GameListEnvironment
import com.micrantha.skouter.ui.games.list.GameListScreen
import com.micrantha.skouter.ui.games.list.GameListScreenModel
import com.micrantha.skouter.ui.login.LoginEnvironment
import com.micrantha.skouter.ui.login.LoginScreen
import com.micrantha.skouter.ui.login.LoginScreenModel
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf
import org.kodein.di.instance

internal fun uiModules() = DI.Module("Skouter UI") {
    bindProviderOf(::MainEnvironment)

    bindProviderOf(::LoginEnvironment)
    bindProvider { LoginScreenModel(instance(), instance()) }
    bindProviderOf(::LoginScreen)

    bindProviderOf(::GameListEnvironment)
    bindProvider { GameListScreenModel(instance(), instance()) }
    bindProviderOf(::GameListScreen)

    bindProviderOf(::GameDetailsEnvironment)
    bindFactory { arg: GameDetailScreenArg -> GameDetailsScreenModel(arg, instance(), instance()) }
    bindFactory { arg: GameDetailScreenArg -> GameDetailsScreen(arg) }

    bindProvider { GameCreateScreenModel(instance()) }
    bindProviderOf(::GameCreateScreen)

    bindProviderOf(::DashboardScreen)
    bindProviderOf(::DashboardEnvironment)
    bindProvider { DashboardScreenModel(instance(), instance()) }
}
