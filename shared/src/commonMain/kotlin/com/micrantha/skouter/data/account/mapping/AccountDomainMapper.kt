package com.micrantha.skouter.data.account.mapping

import com.micrantha.skouter.data.account.model.AccountResponse
import com.micrantha.skouter.data.player.mapping.PlayerDomainMapper
import com.micrantha.skouter.domain.model.Session

class AccountDomainMapper(
    private val mapper: PlayerDomainMapper
) {

    fun map(data: AccountResponse) = Session(
        accessToken = data.accessToken,
        refreshToken = data.refreshToken,
        player = mapper.map(data.player)
    )
}
