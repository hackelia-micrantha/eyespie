package com.micrantha.bluebell.ui

import com.micrantha.bluebell.ui.screen.BluebellScreenContext
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

internal fun bluebellUi() = DI.Module(name = "Bluebell UI") {

    bindSingleton { BluebellScreenContext(di, instance(), instance(), instance()) }

}
