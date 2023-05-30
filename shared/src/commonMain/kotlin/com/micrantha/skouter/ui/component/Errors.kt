package com.micrantha.skouter.ui.component

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.micrantha.skouter.ui.component.Strings.NetworkFailure
import com.micrantha.skouter.ui.component.Strings.ResultStatusFailure

fun Throwable.toi18n() = when (this) {
    is ApolloNetworkException -> NetworkFailure
    else -> ResultStatusFailure
}
