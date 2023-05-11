package com.micrantha.skouter.ui.components

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.micrantha.skouter.ui.components.i18n.NetworkFailure
import com.micrantha.skouter.ui.components.i18n.ResultStatusFailure

fun Throwable.toi18n() = when (this) {
    is ApolloNetworkException -> NetworkFailure
    else -> ResultStatusFailure
}
