package com.micrantha.skouter.data

import com.micrantha.skouter.data.account.AccountDataRepository
import com.micrantha.skouter.data.account.mapping.AccountDomainMapper
import com.micrantha.skouter.data.account.model.CurrentAccount
import com.micrantha.skouter.data.account.source.AccountRemoteSource
import com.micrantha.skouter.data.game.GameDataRepository
import com.micrantha.skouter.data.game.mapping.GameDomainMapper
import com.micrantha.skouter.data.game.source.GameRemoteSource
import com.micrantha.skouter.data.local.mapping.DomainMapper
import com.micrantha.skouter.data.player.PlayerDataRepository
import com.micrantha.skouter.data.player.mapping.PlayerDomainMapper
import com.micrantha.skouter.data.player.source.PlayerRemoteSource
import com.micrantha.skouter.data.remote.MicranthaClient
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.data.service.LocationRepository
import com.micrantha.skouter.data.service.PresenceListener
import com.micrantha.skouter.data.storage.StorageDataRepository
import com.micrantha.skouter.data.storage.source.StorageLocalSource
import com.micrantha.skouter.data.storage.source.StorageRemoteSource
import com.micrantha.skouter.data.thing.ThingDataRepository
import com.micrantha.skouter.data.thing.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.thing.source.ThingsRemoteSource
import org.kodein.di.DI
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingletonOf
import org.kodein.di.delegate

internal fun dataModules() = DI.Module("Skouter Data") {
    bindSingletonOf(::SupaClient)

    bindSingletonOf(::MicranthaClient)

    bindProviderOf(::DomainMapper)

    bindProviderOf(::GameDomainMapper)
    bindProviderOf(::GameRemoteSource)
    bindProviderOf(::GameDataRepository)

    bindProviderOf(::ThingsDomainMapper)
    bindProviderOf(::ThingsRemoteSource)
    bindProviderOf(::ThingDataRepository)

    bindProviderOf(::AccountRemoteSource)
    bindProviderOf(::AccountDomainMapper)
    bindProviderOf(::AccountDataRepository)

    bindSingletonOf(::CurrentAccount)

    bindProviderOf(::StorageLocalSource)
    bindProviderOf(::StorageRemoteSource)
    bindProviderOf(::StorageDataRepository)

    bindProviderOf(::PlayerRemoteSource)
    bindProviderOf(::PlayerDomainMapper)
    bindProviderOf(::PlayerDataRepository)

    bindProviderOf(::LocationRepository)

    delegate<PresenceListener>().to<LocationRepository>()
}
