package com.micrantha.skouter.data

import com.micrantha.bluebell.get
import com.micrantha.skouter.data.account.AccountDataRepository
import com.micrantha.skouter.data.account.mapping.AccountDomainMapper
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.data.account.source.AccountRemoteSource
import com.micrantha.skouter.data.client.SupaClient
import com.micrantha.skouter.data.client.SupaRealtimeClient
import com.micrantha.skouter.data.clue.ColorDataRepository
import com.micrantha.skouter.data.clue.LabelDataRepository
import com.micrantha.skouter.data.clue.MatchDataRepository
import com.micrantha.skouter.data.clue.ObjectDataRepository
import com.micrantha.skouter.data.clue.SegmentDataRepository
import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.source.ColorStreamLocalSource
import com.micrantha.skouter.data.clue.source.LabelStreamLocalSource
import com.micrantha.skouter.data.clue.source.MatchStreamLocalSource
import com.micrantha.skouter.data.clue.source.ObjectStreamLocalSource
import com.micrantha.skouter.data.clue.source.SegmentStreamLocalSource
import com.micrantha.skouter.data.game.GameDataRepository
import com.micrantha.skouter.data.game.mapping.GameDomainMapper
import com.micrantha.skouter.data.game.source.GameRemoteSource
import com.micrantha.skouter.data.player.PlayerDataRepository
import com.micrantha.skouter.data.player.mapping.PlayerDomainMapper
import com.micrantha.skouter.data.player.source.PlayerRemoteSource
import com.micrantha.skouter.data.storage.StorageDataRepository
import com.micrantha.skouter.data.storage.source.StorageLocalSource
import com.micrantha.skouter.data.storage.source.StorageRemoteSource
import com.micrantha.skouter.data.system.LocationDataRepository
import com.micrantha.skouter.data.system.RealtimeDataRepository
import com.micrantha.skouter.data.system.mapping.LocationDomainMapper
import com.micrantha.skouter.data.system.mapping.RealtimeDomainMapper
import com.micrantha.skouter.data.system.source.LocationLocalSource
import com.micrantha.skouter.data.system.source.RealtimeRemoteSource
import com.micrantha.skouter.data.thing.ThingDataRepository
import com.micrantha.skouter.data.thing.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.thing.source.ThingsRemoteSource
import com.micrantha.skouter.platform.scan.AnalyzerCallback
import com.micrantha.skouter.platform.scan.model.ImageColors
import com.micrantha.skouter.platform.scan.model.ImageEmbeddings
import com.micrantha.skouter.platform.scan.model.ImageLabels
import com.micrantha.skouter.platform.scan.model.ImageObjects
import com.micrantha.skouter.platform.scan.model.ImageSegments
import dev.icerock.moko.geo.LocationTracker
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.delegate

internal fun dataModules() = DI.Module("Skouter Data") {
    bindSingletonOf(::SupaClient)
    bindSingletonOf(::SupaRealtimeClient)

    bindProviderOf(::GameDomainMapper)
    bindProviderOf(::GameRemoteSource)
    bindProviderOf(::GameDataRepository)

    bindProviderOf(::ThingsDomainMapper)
    bindProviderOf(::ThingsRemoteSource)
    bindProviderOf(::ThingDataRepository)

    bindProviderOf(::AccountRemoteSource)
    bindProviderOf(::AccountDataRepository)

    bindProviderOf(::AccountDomainMapper)

    bindSingleton { CurrentSession }

    bindProviderOf(::StorageLocalSource)
    bindProviderOf(::StorageRemoteSource)
    bindProviderOf(::StorageDataRepository)

    bindProviderOf(::PlayerRemoteSource)
    bindProviderOf(::PlayerDomainMapper)
    bindProviderOf(::PlayerDataRepository)

    bindFactory { callback: AnalyzerCallback<ImageLabels> ->
        LabelStreamLocalSource(di, callback)
    }
    bindFactory { callback: AnalyzerCallback<ImageColors> ->
        ColorStreamLocalSource(di, callback)
    }
    bindFactory { callback: AnalyzerCallback<ImageObjects> ->
        ObjectStreamLocalSource(
            di,
            callback
        )
    }
    bindFactory { callback: AnalyzerCallback<ImageSegments> ->
        SegmentStreamLocalSource(di, callback)
    }
    bindFactory { callback: AnalyzerCallback<ImageEmbeddings> ->
        MatchStreamLocalSource(di, callback)
    }

    bindProvider { LabelDataRepository(di, get(), get()) }
    bindProvider { SegmentDataRepository(di, get(), get()) }
    bindProvider { ColorDataRepository(di, get(), get()) }
    bindProvider { ObjectDataRepository(di, get(), get()) }
    bindProvider { MatchDataRepository(di, get(), get()) }
    bindProviderOf(::ClueDomainMapper)

    bindProviderOf(::LocationDomainMapper)
    bindProviderOf(::LocationDataRepository)
    bindProviderOf(::RealtimeDataRepository)
    bindSingletonOf(::RealtimeRemoteSource)
    bindProviderOf(::RealtimeDomainMapper)

    delegate<LocationLocalSource>().to<LocationTracker>()
}
