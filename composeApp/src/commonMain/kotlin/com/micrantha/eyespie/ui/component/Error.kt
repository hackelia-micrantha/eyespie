package com.micrantha.eyespie.ui.component

import com.apollographql.apollo.exception.ApolloNetworkException
import com.micrantha.eyespie.ui.component.Strings.NetworkFailure
import com.micrantha.eyespie.ui.component.Strings.ResultStatusFailure

fun Throwable.toi18n() = when (this) {
    is ApolloNetworkException -> NetworkFailure
    else -> ResultStatusFailure
}
