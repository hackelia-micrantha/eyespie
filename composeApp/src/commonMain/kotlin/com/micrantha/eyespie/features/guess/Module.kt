package com.micrantha.eyespie.features.guess

import com.micrantha.bluebell.get
import com.micrantha.eyespie.features.guess.ui.ScanGuessArgs
import com.micrantha.eyespie.features.guess.ui.ScanGuessEnvironment
import com.micrantha.eyespie.features.guess.ui.ScanGuessMapper
import com.micrantha.eyespie.features.guess.ui.ScanGuessScreen
import com.micrantha.eyespie.features.guess.ui.ScanGuessScreenModel
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProviderOf


internal fun module() = DI.Module("Guess") {
    bindProviderOf(::ScanGuessMapper)
    bindFactory { args: ScanGuessArgs ->
        ScanGuessScreenModel(args, get(), get())
    }
    bindFactory { args: ScanGuessArgs -> ScanGuessScreen(args) }
    bindFactory { args: ScanGuessArgs ->
        ScanGuessEnvironment(args, get(), get(), get())
    }

}
