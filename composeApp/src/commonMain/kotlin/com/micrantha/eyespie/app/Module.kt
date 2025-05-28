package com.micrantha.eyespie.app

import com.micrantha.eyespie.app.ui.MainScreen
import com.micrantha.eyespie.app.ui.MainScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProviderOf
import com.micrantha.eyespie.core.module as coreModule
import com.micrantha.eyespie.domain.module as domainModule
import com.micrantha.eyespie.features.module as featuresModule

internal fun module() = DI.Module("App") {
    importOnce(coreModule())
    importOnce(domainModule())
    importOnce(featuresModule())

    bindProviderOf(::MainScreenModel)
    bindProviderOf(::MainScreen)
}
