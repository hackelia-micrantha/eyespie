package com.micrantha.eyespie.data.account.mapping

import com.micrantha.eyespie.data.account.model.AccountResponse
import com.micrantha.eyespie.data.player.mapping.PlayerDomainMapper
import com.micrantha.eyespie.domain.model.Session

class AccountDomainMapper(
    private val mapper: PlayerDomainMapper
) {

    fun map(data: AccountResponse) = Session(
        accessToken = data.accessToken,
        refreshToken = data.refreshToken,
        player = mapper.map(data.player)
    )
}
