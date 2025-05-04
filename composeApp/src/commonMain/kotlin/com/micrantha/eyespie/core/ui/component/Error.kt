package com.micrantha.eyespie.core.ui.component

import com.apollographql.apollo.exception.ApolloNetworkException
import com.micrantha.eyespie.app.Strings.NetworkFailure
import com.micrantha.eyespie.app.Strings.ResultStatusFailure

fun Throwable.toi18n() = when (this) {
    is ApolloNetworkException -> NetworkFailure
    else -> ResultStatusFailure
}
