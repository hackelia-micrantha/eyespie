package com.micrantha.eyespie.features

import org.kodein.di.DI
import com.micrantha.eyespie.features.dashboard.module as dashboardModule
import com.micrantha.eyespie.features.game.module as gameModule
import com.micrantha.eyespie.features.guess.module as guessModule
import com.micrantha.eyespie.features.login.module as loginModule
import com.micrantha.eyespie.features.register.module as registerModule
import com.micrantha.eyespie.features.players.module as playersModule
import com.micrantha.eyespie.features.scan.module as scanModule
import com.micrantha.eyespie.features.things.module as thingsModule

// planning for downloadable features
internal fun module() = DI.Module("Features") {
    importOnce(dashboardModule())
    importOnce(gameModule())
    importOnce(scanModule())
    importOnce(guessModule())
    importOnce(loginModule())
    importOnce(playersModule())
    importOnce(thingsModule())
    importOnce(registerModule())
}