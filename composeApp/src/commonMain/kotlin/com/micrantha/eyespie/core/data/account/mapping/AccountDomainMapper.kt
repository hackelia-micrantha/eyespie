package com.micrantha.eyespie.core.data.account.mapping

import com.micrantha.eyespie.core.data.account.model.AccountResponse
import com.micrantha.eyespie.domain.entities.Session

class AccountDomainMapper(
) {

    fun map(data: AccountResponse) = Session(
        accessToken = data.accessToken,
        refreshToken = data.refreshToken,
        userId = data.userId
    )
}
