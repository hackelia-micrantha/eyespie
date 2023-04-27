package com.micrantha.bluebell.ui

import com.micrantha.bluebell.ui.view.DefaultViewContext
import com.micrantha.bluebell.ui.view.ViewContext
import org.koin.dsl.module

internal fun bluebellUi() = module {
    factory<ViewContext> { params -> DefaultViewContext(params.get(), get(), get()) }
}
