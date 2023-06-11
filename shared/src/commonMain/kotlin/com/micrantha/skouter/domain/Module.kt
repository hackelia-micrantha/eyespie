package com.micrantha.skouter.domain

import com.micrantha.skouter.domain.logic.GameLogic
import org.kodein.di.DI
import org.kodein.di.bindSingletonOf

internal fun domainModules() = DI.Module("Skouter Domain") {
    bindSingletonOf(::GameLogic)
}
