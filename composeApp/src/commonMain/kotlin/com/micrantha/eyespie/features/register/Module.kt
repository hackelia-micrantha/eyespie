package com.micrantha.eyespie.features.register

import com.micrantha.bluebell.get
import com.micrantha.eyespie.features.register.ui.RegisterEnvironment
import com.micrantha.eyespie.features.register.ui.RegisterScreen
import com.micrantha.eyespie.features.register.ui.RegisterScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf

internal fun module() = DI.Module("Register") {
    bindProviderOf(::RegisterScreen)
    bindProviderOf(::RegisterEnvironment)
    bindProvider { RegisterScreenModel(get(), get()) }
}