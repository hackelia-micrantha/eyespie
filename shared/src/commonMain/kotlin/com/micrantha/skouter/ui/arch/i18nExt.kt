package com.micrantha.skouter.ui.arch

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.micrantha.skouter.ui.arch.i18n.NetworkFailure
import com.micrantha.skouter.ui.arch.i18n.ResultStatusFailure

fun Throwable.toi18n() = when (this) {
    is ApolloNetworkException -> NetworkFailure
    else -> ResultStatusFailure
}
