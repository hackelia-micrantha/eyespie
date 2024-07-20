package com.micrantha.eyespie.data

import com.micrantha.bluebell.get
import com.micrantha.eyespie.data.account.AccountDataRepository
import com.micrantha.eyespie.data.account.mapping.AccountDomainMapper
import com.micrantha.eyespie.data.account.model.CurrentSession
import com.micrantha.eyespie.data.account.source.AccountRemoteSource
import com.micrantha.eyespie.data.client.SupaClient
import com.micrantha.eyespie.data.client.SupaRealtimeClient
import com.micrantha.eyespie.data.clue.ColorDataRepository
import com.micrantha.eyespie.data.clue.DetectDataRepository
import com.micrantha.eyespie.data.clue.LabelDataRepository
import com.micrantha.eyespie.data.clue.MatchDataRepository
import com.micrantha.eyespie.data.clue.SegmentDataRepository
import com.micrantha.eyespie.data.clue.mapping.ClueDomainMapper
import com.micrantha.eyespie.data.clue.source.LabelRemoteSource
import com.micrantha.eyespie.data.game.GameDataRepository
import com.micrantha.eyespie.data.game.mapping.GameDomainMapper
import com.micrantha.eyespie.data.game.source.GameRemoteSource
import com.micrantha.eyespie.data.player.PlayerDataRepository
import com.micrantha.eyespie.data.player.mapping.PlayerDomainMapper
import com.micrantha.eyespie.data.player.source.PlayerRemoteSource
import com.micrantha.eyespie.data.storage.StorageDataRepository
import com.micrantha.eyespie.data.storage.source.StorageLocalSource
import com.micrantha.eyespie.data.storage.source.StorageRemoteSource
import com.micrantha.eyespie.data.system.LocationDataRepository
import com.micrantha.eyespie.data.system.RealtimeDataRepository
import com.micrantha.eyespie.data.system.mapping.LocationDomainMapper
import com.micrantha.eyespie.data.system.mapping.RealtimeDomainMapper
import com.micrantha.eyespie.data.system.source.LocationLocalSource
import com.micrantha.eyespie.data.system.source.RealtimeRemoteSource
import com.micrantha.eyespie.data.thing.ThingDataRepository
import com.micrantha.eyespie.data.thing.mapping.ThingsDomainMapper
import com.micrantha.eyespie.data.thing.source.ThingsRemoteSource
import dev.icerock.moko.geo.LocationTracker
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.delegate

internal fun dataModules() = DI.Module("EyesPie Data") {
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
