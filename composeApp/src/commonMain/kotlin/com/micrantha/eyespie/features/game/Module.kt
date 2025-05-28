package com.micrantha.eyespie.features.game

import com.micrantha.bluebell.get
import com.micrantha.eyespie.features.game.data.GameDataRepository
import com.micrantha.eyespie.features.game.data.mapping.GameDomainMapper
import com.micrantha.eyespie.features.game.data.source.GameRemoteSource
import com.micrantha.eyespie.features.game.ui.create.GameCreateScreen
import com.micrantha.eyespie.features.game.ui.create.GameCreateScreenModel
import com.micrantha.eyespie.features.game.ui.detail.GameDetailScreenArg
import com.micrantha.eyespie.features.game.ui.detail.GameDetailsEnvironment
import com.micrantha.eyespie.features.game.ui.detail.GameDetailsScreen
import com.micrantha.eyespie.features.game.ui.detail.GameDetailsScreenModel
import com.micrantha.eyespie.features.game.ui.list.GameListEnvironment
import com.micrantha.eyespie.features.game.ui.list.GameListScreen
import com.micrantha.eyespie.features.game.ui.list.GameListScreenModel
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProviderOf

internal fun module() = DI.Module("Game") {

    bindProviderOf(::GameDomainMapper)
    bindProviderOf(::GameRemoteSource)
    bindProviderOf(::GameDataRepository)

    bindProviderOf(::GameListEnvironment)
    bindProviderOf(::GameListScreenModel)
    bindProviderOf(::GameListScreen)

    bindProviderOf(::GameDetailsEnvironment)
    bindProviderOf(::GameDetailsScreenModel)
    bindFactory { arg: GameDetailScreenArg -> GameDetailsScreen(get(), arg) }

    bindProviderOf(::GameCreateScreenModel)
    bindProviderOf(::GameCreateScreen)

}
