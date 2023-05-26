package com.micrantha.skouter

import com.micrantha.skouter.data.dataModules
import com.micrantha.skouter.domain.domainModules
import com.micrantha.skouter.ui.uiModules
import org.kodein.di.DI


fun skouterModules() = DI.Module("Skouter") {
    import(dataModules())
    import(domainModules())
    import(uiModules())
}
