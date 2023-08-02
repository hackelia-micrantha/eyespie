package com.micrantha.skouter

import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.data.account.AccountDataRepository
import com.micrantha.skouter.data.clue.ColorDataRepository
import com.micrantha.skouter.data.clue.LabelDataRepository
import com.micrantha.skouter.data.clue.MatchDataRepository
import com.micrantha.skouter.data.clue.ObjectDataRepository
import com.micrantha.skouter.data.clue.SegmentDataRepository
import com.micrantha.skouter.data.game.GameDataRepository
import com.micrantha.skouter.data.player.PlayerDataRepository
import com.micrantha.skouter.data.storage.StorageDataRepository
import com.micrantha.skouter.data.system.LocationDataRepository
import com.micrantha.skouter.data.system.RealtimeDataRepository
import com.micrantha.skouter.data.thing.ThingDataRepository
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.domain.repository.ColorRepository
import com.micrantha.skouter.domain.repository.DetectionRepository
import com.micrantha.skouter.domain.repository.GameRepository
import com.micrantha.skouter.domain.repository.LabelRepository
import com.micrantha.skouter.domain.repository.LocationRepository
import com.micrantha.skouter.domain.repository.MatchRepository
import com.micrantha.skouter.domain.repository.PlayerRepository
import com.micrantha.skouter.domain.repository.RealtimeRepository
import com.micrantha.skouter.domain.repository.SegmentRepository
import com.micrantha.skouter.domain.repository.StorageRepository
import com.micrantha.skouter.domain.repository.ThingRepository
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.analyzer.ColorCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.ColorStreamAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.EmbeddingCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.EmbeddingStreamAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.LabelCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.LabelStreamAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.ObjectCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.ObjectStreamAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.SegmentCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.SegmentStreamAnalyzer
import com.micrantha.skouter.platform.scan.model.ImageColors
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings
import com.micrantha.skouter.platform.scan.model.ImageLabels
import com.micrantha.skouter.platform.scan.model.ImageObjects
import com.micrantha.skouter.platform.scan.model.ImageSegments
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingleton
import org.kodein.di.delegate

fun iosModules(app: UIApplicationController) = DI {

    bindSingleton { Platform(app) }

    bindFactory { callback: AnalyzerCallback<ImageLabels> ->
        LabelStreamAnalyzer(callback)
    }
    bindProviderOf(::LabelCaptureAnalyzer)

    bindProviderOf(::ColorCaptureAnalyzer)
    bindFactory { callback: AnalyzerCallback<ImageColors> ->
        ColorStreamAnalyzer(callback)
    }
    bindProviderOf(::ObjectCaptureAnalyzer)
    bindFactory { callback: AnalyzerCallback<ImageObjects> ->
        ObjectStreamAnalyzer(callback)
    }
    bindProviderOf(::SegmentCaptureAnalyzer)
    bindFactory { callback: AnalyzerCallback<ImageSegments> ->
        SegmentStreamAnalyzer(callback)
    }
    bindProviderOf(::EmbeddingCaptureAnalyzer)
    bindFactory { callback: AnalyzerCallback<ImageEmbeddings> ->
        EmbeddingStreamAnalyzer(callback)
    }

    delegate<AccountRepository>().to<AccountDataRepository>()
    delegate<ThingRepository>().to<ThingDataRepository>()
    delegate<LabelRepository>().to<LabelDataRepository>()
    delegate<ColorRepository>().to<ColorDataRepository>()
    delegate<MatchRepository>().to<MatchDataRepository>()
    delegate<DetectionRepository>().to<ObjectDataRepository>()
    delegate<SegmentRepository>().to<SegmentDataRepository>()
    delegate<GameRepository>().to<GameDataRepository>()
    delegate<LocationRepository>().to<LocationDataRepository>()
    delegate<PlayerRepository>().to<PlayerDataRepository>()
    delegate<RealtimeRepository>().to<RealtimeDataRepository>()
    delegate<StorageRepository>().to<StorageDataRepository>()
}
