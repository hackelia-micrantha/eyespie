package com.micrantha.eyespie.domain

import com.micrantha.eyespie.domain.logic.GameLogic
import org.kodein.di.DI
import org.kodein.di.bindSingletonOf

internal fun domainModules() = DI.Module("EyesPie Domain") {
    bindSingletonOf(::GameLogic)
}
