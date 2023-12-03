package com.micrantha.skouter.ui

import com.micrantha.bluebell.get
import com.micrantha.skouter.ui.dashboard.DashboardEnvironment
import com.micrantha.skouter.ui.dashboard.DashboardScreen
import com.micrantha.skouter.ui.dashboard.DashboardScreenModel
import com.micrantha.skouter.ui.dashboard.usecase.DashboardLoadUseCase
import com.micrantha.skouter.ui.game.create.GameCreateScreen
import com.micrantha.skouter.ui.game.create.GameCreateScreenModel
import com.micrantha.skouter.ui.game.detail.GameDetailScreenArg
import com.micrantha.skouter.ui.game.detail.GameDetailsEnvironment
import com.micrantha.skouter.ui.game.detail.GameDetailsScreen
import com.micrantha.skouter.ui.game.detail.GameDetailsScreenModel
import com.micrantha.skouter.ui.game.list.GameListEnvironment
import com.micrantha.skouter.ui.game.list.GameListScreen
import com.micrantha.skouter.ui.game.list.GameListScreenModel
import com.micrantha.skouter.ui.login.LoginEnvironment
import com.micrantha.skouter.ui.login.LoginScreen
import com.micrantha.skouter.ui.login.LoginScreenModel
import com.micrantha.skouter.ui.scan.capture.ScanCaptureEnvironment
import com.micrantha.skouter.ui.scan.capture.ScanCaptureScreen
import com.micrantha.skouter.ui.scan.capture.ScanCaptureScreenModel
import com.micrantha.skouter.ui.scan.capture.ScanCaptureStateMapper
import com.micrantha.skouter.ui.scan.edit.ScanEditEnvironment
import com.micrantha.skouter.ui.scan.edit.ScanEditScreenModel
import com.micrantha.skouter.ui.scan.guess.ScanGuessArgs
import com.micrantha.skouter.ui.scan.guess.ScanGuessEnvironment
import com.micrantha.skouter.ui.scan.guess.ScanGuessMapper
import com.micrantha.skouter.ui.scan.guess.ScanGuessScreen
import com.micrantha.skouter.ui.scan.guess.ScanGuessScreenModel
import com.micrantha.skouter.ui.scan.usecase.AnalyzeCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.GetEditCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.MatchCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.SaveCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.SubAnalyzeClueUseCase
import com.micrantha.skouter.ui.scan.usecase.TakeCaptureUseCase
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf

internal fun uiModules() = DI.Module("Skouter UI") {
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
    bindFactory { scope: CoroutineScope -> DashboardEnvironment(scope, get(), get()) }
    bindProvider { DashboardScreenModel(get()) }

    bindProviderOf(::ScanCaptureScreen)
    bindProviderOf(::ScanCaptureStateMapper)
    bindProviderOf(::ScanCaptureScreenModel)
    bindFactory { scope: CoroutineScope ->
        ScanCaptureEnvironment(
            scope, get(), get(), get(), get(), get(), get(), get(), get()
        )
    }

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
