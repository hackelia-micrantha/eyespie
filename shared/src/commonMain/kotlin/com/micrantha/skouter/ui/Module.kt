package com.micrantha.skouter.ui

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
import com.micrantha.skouter.ui.scan.edit.ScanEditEnvironment
import com.micrantha.skouter.ui.scan.edit.ScanEditScreenModel
import com.micrantha.skouter.ui.scan.preview.ScanEnvironment
import com.micrantha.skouter.ui.scan.preview.ScanScreen
import com.micrantha.skouter.ui.scan.preview.ScanScreenModel
import com.micrantha.skouter.ui.scan.usecase.AnalyzeCameraImageUseCase
import com.micrantha.skouter.ui.scan.usecase.CameraCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.LoadCameraImageUseCase
import com.micrantha.skouter.ui.scan.usecase.SaveThingImageUseCase
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf
import org.kodein.di.instance

internal fun uiModules() = DI.Module("Skouter UI") {
    bindProviderOf(::MainScreenModel)
    bindProviderOf(::MainScreen)

    bindProviderOf(::LoginEnvironment)
    bindProvider { LoginScreenModel(instance(), instance()) }
    bindProviderOf(::LoginScreen)

    bindProviderOf(::GameListEnvironment)
    bindProvider { GameListScreenModel(instance(), instance()) }
    bindProvider { GameListScreen(instance()) }

    bindProviderOf(::GameDetailsEnvironment)
    bindProvider { GameDetailsScreenModel(instance(), instance()) }
    bindFactory { arg: GameDetailScreenArg -> GameDetailsScreen(arg) }

    bindProvider { GameCreateScreenModel(instance()) }
    bindProviderOf(::GameCreateScreen)

    bindProviderOf(::DashboardScreen)
    bindProviderOf(::DashboardLoadUseCase)
    bindProviderOf(::DashboardEnvironment)
    bindProvider { DashboardScreenModel(instance(), instance()) }

    bindProviderOf(::ScanScreen)
    bindProviderOf(::ScanEnvironment)
    bindProviderOf(::ScanScreenModel)

    bindProviderOf(::ScanEditEnvironment)
    bindProviderOf(::ScanEditScreenModel)

    bindProviderOf(::CameraCaptureUseCase)
    bindProviderOf(::SaveThingImageUseCase)
    bindProviderOf(::AnalyzeCameraImageUseCase)
    bindProviderOf(::LoadCameraImageUseCase)
}
