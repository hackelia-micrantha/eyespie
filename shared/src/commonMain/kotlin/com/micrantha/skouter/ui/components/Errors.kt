package com.micrantha.skouter.ui.components

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.micrantha.skouter.ui.components.Strings.NetworkFailure
import com.micrantha.skouter.ui.components.Strings.ResultStatusFailure

fun Throwable.toi18n() = when (this) {
    is ApolloNetworkException -> NetworkFailure
    else -> ResultStatusFailure
}
