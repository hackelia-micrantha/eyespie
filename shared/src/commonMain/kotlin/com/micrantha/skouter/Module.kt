package com.micrantha.skouter

import com.micrantha.skouter.data.dataModules
import com.micrantha.skouter.ui.uiModules
import org.koin.dsl.module

fun skouterModules() = module {
    includes(dataModules(), uiModules())
}
