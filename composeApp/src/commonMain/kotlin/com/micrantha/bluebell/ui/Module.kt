package com.micrantha.bluebell.ui

import com.micrantha.bluebell.app.navi.BluebellRouter
import com.micrantha.bluebell.get
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.BluebellScreenContext
import com.micrantha.bluebell.ui.screen.ScaffoldScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingleton

internal fun bluebellUi() = DI.Module(name = "Bluebell UI") {

    bindSingleton<Router> { BluebellRouter(get()) }
    bindSingleton<ScreenContext> { BluebellScreenContext(di, get(), get(), get()) }

    bindProviderOf(::ScaffoldScreenModel)
}
