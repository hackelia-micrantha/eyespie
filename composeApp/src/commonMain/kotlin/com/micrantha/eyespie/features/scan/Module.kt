package com.micrantha.eyespie.features.scan

import com.micrantha.eyespie.features.scan.data.ColorDataRepository
import com.micrantha.eyespie.features.scan.data.DetectDataRepository
import com.micrantha.eyespie.features.scan.data.LabelDataRepository
import com.micrantha.eyespie.features.scan.data.MatchDataRepository
import com.micrantha.eyespie.features.scan.data.SegmentDataRepository
import com.micrantha.eyespie.features.scan.data.mapping.ClueDomainMapper
import com.micrantha.eyespie.features.scan.ui.capture.ScanCaptureEnvironment
import com.micrantha.eyespie.features.scan.ui.capture.ScanCaptureScreen
import com.micrantha.eyespie.features.scan.ui.capture.ScanCaptureScreenModel
import com.micrantha.eyespie.features.scan.ui.capture.ScanCaptureStateMapper
import com.micrantha.eyespie.features.scan.ui.edit.ScanEditEnvironment
import com.micrantha.eyespie.features.scan.ui.edit.ScanEditScreen
import com.micrantha.eyespie.features.scan.ui.edit.ScanEditScreenModel
import com.micrantha.eyespie.features.scan.ui.usecase.AnalyzeCaptureUseCase
import com.micrantha.eyespie.features.scan.ui.usecase.GetEditCaptureUseCase
import com.micrantha.eyespie.features.scan.ui.usecase.MatchCaptureUseCase
import com.micrantha.eyespie.features.scan.ui.usecase.SaveCaptureUseCase
import com.micrantha.eyespie.features.scan.ui.usecase.SubAnalyzeClueUseCase
import com.micrantha.eyespie.features.scan.ui.usecase.TakeCaptureUseCase
import org.kodein.di.DI
import org.kodein.di.bindProviderOf


internal fun module() = DI.Module("Scan") {

    bindProviderOf(::ClueDomainMapper)
    bindProviderOf(::ColorDataRepository)
    bindProviderOf(::DetectDataRepository)
    bindProviderOf(::LabelDataRepository)
    bindProviderOf(::SegmentDataRepository)
    bindProviderOf(::MatchDataRepository)

    bindProviderOf(::TakeCaptureUseCase)
    bindProviderOf(::SaveCaptureUseCase)
    bindProviderOf(::MatchCaptureUseCase)
    bindProviderOf(::AnalyzeCaptureUseCase)
    bindProviderOf(::GetEditCaptureUseCase)
    bindProviderOf(::SubAnalyzeClueUseCase)

    bindProviderOf(::ScanCaptureStateMapper)
    bindProviderOf(::ScanCaptureEnvironment)
    bindProviderOf(::ScanCaptureScreenModel)
    bindProviderOf(::ScanCaptureScreen)

    bindProviderOf(::ScanEditEnvironment)
    bindProviderOf(::ScanEditScreenModel)
    bindProviderOf(::ScanEditScreen)
}
