package com.micrantha.eyespie

import com.micrantha.bluebell.platform.Platform
import com.micrantha.eyespie.core.data.account.AccountDataRepository
import com.micrantha.eyespie.core.data.storage.StorageDataRepository
import com.micrantha.eyespie.core.data.system.LocationDataRepository
import com.micrantha.eyespie.core.data.system.RealtimeDataRepository
import com.micrantha.eyespie.domain.repository.AccountRepository
import com.micrantha.eyespie.domain.repository.ColorRepository
import com.micrantha.eyespie.domain.repository.DetectRepository
import com.micrantha.eyespie.domain.repository.GameRepository
import com.micrantha.eyespie.domain.repository.LabelRepository
import com.micrantha.eyespie.domain.repository.LocationRepository
import com.micrantha.eyespie.domain.repository.MatchRepository
import com.micrantha.eyespie.domain.repository.RealtimeRepository
import com.micrantha.eyespie.domain.repository.SegmentRepository
import com.micrantha.eyespie.domain.repository.StorageRepository
import com.micrantha.eyespie.domain.repository.ThingRepository
import com.micrantha.eyespie.features.game.data.GameDataRepository
import com.micrantha.eyespie.features.players.data.PlayerDataRepository
import com.micrantha.eyespie.features.players.domain.repository.PlayerRepository
import com.micrantha.eyespie.features.scan.data.ColorDataRepository
import com.micrantha.eyespie.features.scan.data.DetectDataRepository
import com.micrantha.eyespie.features.scan.data.LabelDataRepository
import com.micrantha.eyespie.features.scan.data.MatchDataRepository
import com.micrantha.eyespie.features.scan.data.SegmentDataRepository
import com.micrantha.eyespie.features.things.data.ThingDataRepository
import com.micrantha.eyespie.platform.scan.analyzer.ColorCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.DetectCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.LabelCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.MatchCaptureAnalyzer
import com.micrantha.eyespie.platform.scan.analyzer.SegmentCaptureAnalyzer
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingleton
import org.kodein.di.delegate

fun iosModules(app: AppDelegate) = DI {

    bindSingleton { Platform(app) }

    bindProvider { app.networkMonitor }

    bindProviderOf(::LabelCaptureAnalyzer)

    bindProviderOf(::ColorCaptureAnalyzer)

    bindProviderOf(::DetectCaptureAnalyzer)

    bindProviderOf(::SegmentCaptureAnalyzer)

    bindProviderOf(::MatchCaptureAnalyzer)

    delegate<AccountRepository>().to<AccountDataRepository>()
    delegate<ThingRepository>().to<ThingDataRepository>()
    delegate<LabelRepository>().to<LabelDataRepository>()
    delegate<ColorRepository>().to<ColorDataRepository>()
    delegate<MatchRepository>().to<MatchDataRepository>()
    delegate<DetectRepository>().to<DetectDataRepository>()
    delegate<SegmentRepository>().to<SegmentDataRepository>()
    delegate<GameRepository>().to<GameDataRepository>()
    delegate<LocationRepository>().to<LocationDataRepository>()
    delegate<PlayerRepository>().to<PlayerDataRepository>()
    delegate<RealtimeRepository>().to<RealtimeDataRepository>()
    delegate<StorageRepository>().to<StorageDataRepository>()
}
