package com.micrantha.eyespie.features.login

import com.micrantha.bluebell.get
import com.micrantha.eyespie.ui.login.LoginEnvironment
import com.micrantha.eyespie.ui.login.LoginScreen
import com.micrantha.eyespie.ui.login.LoginScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf

internal fun module() = DI.Module("Login") {
    bindProviderOf(::LoginEnvironment)
    bindProvider { LoginScreenModel(get(), get()) }
    bindProviderOf(::LoginScreen)
}
