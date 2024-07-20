package com.micrantha.eyespie

import com.micrantha.eyespie.data.dataModules
import com.micrantha.eyespie.domain.domainModules
import com.micrantha.eyespie.ui.uiModules
import org.kodein.di.DI


fun eyespieModules() = DI.Module("EyesPie") {
    importOnce(dataModules())
    importOnce(domainModules())
    importOnce(uiModules())
}
