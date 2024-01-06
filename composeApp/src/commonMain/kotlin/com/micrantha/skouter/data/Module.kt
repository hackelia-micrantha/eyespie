package com.micrantha.skouter.data

import com.micrantha.bluebell.get
import com.micrantha.skouter.data.account.AccountDataRepository
import com.micrantha.skouter.data.account.mapping.AccountDomainMapper
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.data.account.source.AccountRemoteSource
import com.micrantha.skouter.data.client.SupaClient
import com.micrantha.skouter.data.client.SupaRealtimeClient
import com.micrantha.skouter.data.clue.ColorDataRepository
import com.micrantha.skouter.data.clue.DetectDataRepository
import com.micrantha.skouter.data.clue.LabelDataRepository
import com.micrantha.skouter.data.clue.MatchDataRepository
import com.micrantha.skouter.data.clue.SegmentDataRepository
import com.micrantha.skouter.data.clue.mapping.ClueDomainMapper
import com.micrantha.skouter.data.clue.source.LabelRemoteSource
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
import dev.icerock.moko.geo.LocationTracker
import org.kodein.di.DI
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

    bindProviderOf(::LabelRemoteSource)

    bindProvider { LabelDataRepository(di, get(), get(), get()) }
    bindProvider { SegmentDataRepository(di, get()) }
    bindProvider { ColorDataRepository(di, get()) }
    bindProvider { DetectDataRepository(di, get()) }
    bindProvider { MatchDataRepository(di, get()) }
    bindProviderOf(::ClueDomainMapper)

    bindProviderOf(::LocationDomainMapper)
    bindProviderOf(::LocationDataRepository)
    bindProviderOf(::RealtimeDataRepository)
    bindSingletonOf(::RealtimeRemoteSource)
    bindProviderOf(::RealtimeDomainMapper)

    delegate<LocationLocalSource>().to<LocationTracker>()
}
