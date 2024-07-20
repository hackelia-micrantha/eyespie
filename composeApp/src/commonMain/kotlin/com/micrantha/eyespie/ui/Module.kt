package com.micrantha.eyespie.ui

import com.micrantha.bluebell.get
import com.micrantha.eyespie.ui.dashboard.DashboardEnvironment
import com.micrantha.eyespie.ui.dashboard.DashboardScreen
import com.micrantha.eyespie.ui.dashboard.DashboardScreenModel
import com.micrantha.eyespie.ui.dashboard.usecase.DashboardLoadUseCase
import com.micrantha.eyespie.ui.game.create.GameCreateScreen
import com.micrantha.eyespie.ui.game.create.GameCreateScreenModel
import com.micrantha.eyespie.ui.game.detail.GameDetailScreenArg
import com.micrantha.eyespie.ui.game.detail.GameDetailsEnvironment
import com.micrantha.eyespie.ui.game.detail.GameDetailsScreen
import com.micrantha.eyespie.ui.game.detail.GameDetailsScreenModel
import com.micrantha.eyespie.ui.game.list.GameListEnvironment
import com.micrantha.eyespie.ui.game.list.GameListScreen
import com.micrantha.eyespie.ui.game.list.GameListScreenModel
import com.micrantha.eyespie.ui.login.LoginEnvironment
import com.micrantha.eyespie.ui.login.LoginScreen
import com.micrantha.eyespie.ui.login.LoginScreenModel
import com.micrantha.eyespie.ui.scan.capture.ScanCaptureEnvironment
import com.micrantha.eyespie.ui.scan.capture.ScanCaptureScreen
import com.micrantha.eyespie.ui.scan.capture.ScanCaptureScreenModel
import com.micrantha.eyespie.ui.scan.capture.ScanCaptureStateMapper
import com.micrantha.eyespie.ui.scan.edit.ScanEditEnvironment
import com.micrantha.eyespie.ui.scan.edit.ScanEditScreenModel
import com.micrantha.eyespie.ui.scan.guess.ScanGuessArgs
import com.micrantha.eyespie.ui.scan.guess.ScanGuessEnvironment
import com.micrantha.eyespie.ui.scan.guess.ScanGuessMapper
import com.micrantha.eyespie.ui.scan.guess.ScanGuessScreen
import com.micrantha.eyespie.ui.scan.guess.ScanGuessScreenModel
import com.micrantha.eyespie.ui.scan.usecase.AnalyzeCaptureUseCase
import com.micrantha.eyespie.ui.scan.usecase.GetEditCaptureUseCase
import com.micrantha.eyespie.ui.scan.usecase.MatchCaptureUseCase
import com.micrantha.eyespie.ui.scan.usecase.SaveCaptureUseCase
import com.micrantha.eyespie.ui.scan.usecase.SubAnalyzeClueUseCase
import com.micrantha.eyespie.ui.scan.usecase.TakeCaptureUseCase
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf

internal fun uiModules() = DI.Module("EyesPie UI") {
    bindProviderOf(::MainScreenModel)
    bindProviderOf(::MainScreen)

    bindProviderOf(::LoginEnvironment)
    bindProvider { LoginScreenModel(get(), get()) }
    bindProviderOf(::LoginScreen)

    bindProviderOf(::GameListEnvironment)
    bindProviderOf(::GameListScreenModel)
    bindProviderOf(::GameListScreen)

    bindProviderOf(::GameDetailsEnvironment)
    bindProviderOf(::GameDetailsScreenModel)
    bindFactory { arg: GameDetailScreenArg -> GameDetailsScreen(arg) }

    bindProviderOf(::GameCreateScreenModel)
    bindProviderOf(::GameCreateScreen)

    bindProviderOf(::DashboardScreen)
    bindProviderOf(::DashboardLoadUseCase)
    bindProviderOf(::DashboardEnvironment)
    bindProvider { DashboardScreenModel(get()) }

    bindProviderOf(::ScanCaptureScreen)
    bindProviderOf(::ScanCaptureStateMapper)
    bindProviderOf(::ScanCaptureScreenModel)
    bindProviderOf(::ScanCaptureEnvironment)

    bindProviderOf(::ScanEditEnvironment)
    bindProviderOf(::ScanEditScreenModel)

    bindProviderOf(::ScanGuessMapper)
    bindFactory { args: ScanGuessArgs ->
        ScanGuessScreenModel(args, get(), get())
    }
    bindFactory { args: ScanGuessArgs -> ScanGuessScreen(args) }
    bindFactory { args: ScanGuessArgs ->
        ScanGuessEnvironment(args, get(), get(), get())
    }

    bindProviderOf(::TakeCaptureUseCase)
    bindProviderOf(::SaveCaptureUseCase)
    bindProviderOf(::MatchCaptureUseCase)
    bindProviderOf(::AnalyzeCaptureUseCase)
    bindProviderOf(::GetEditCaptureUseCase)
    bindProviderOf(::SubAnalyzeClueUseCase)
}
