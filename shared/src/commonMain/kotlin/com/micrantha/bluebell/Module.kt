package com.micrantha.bluebell

import com.micrantha.bluebell.domain.bluebellDomain
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.ui.bluebellUi
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import org.kodein.di.DI
import org.kodein.di.delegate

fun bluebellModules() = DI.Module(name = "Bluebell") {
    import(bluebellDomain())
    import(bluebellUi())

    delegate<LocalizedRepository>().to<Platform>()
    delegate<Router>().to<ScreenContext>()
}
