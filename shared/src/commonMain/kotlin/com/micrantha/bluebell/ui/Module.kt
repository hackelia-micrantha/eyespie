package com.micrantha.bluebell.ui

import com.micrantha.bluebell.ui.view.ViewContext
import org.koin.dsl.module

internal fun bluebellUi() = module {
    factory { params -> ViewContext(params.get(), get(), get()) }

    factory { params -> MainViewModel(params.get()) }
}
