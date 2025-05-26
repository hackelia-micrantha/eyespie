package com.micrantha.eyespie.core.data.account.mapping

import com.micrantha.eyespie.core.data.account.model.AccountResponse
import com.micrantha.eyespie.domain.entities.Session
import com.micrantha.eyespie.features.players.data.mapping.PlayerDomainMapper

class AccountDomainMapper(
    private val mapper: PlayerDomainMapper
) {

    fun map(data: AccountResponse) = Session(
        accessToken = data.accessToken,
        refreshToken = data.refreshToken,
        player = mapper.map(data.player)
    )
}
