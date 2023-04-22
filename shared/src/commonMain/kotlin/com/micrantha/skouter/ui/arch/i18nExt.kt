package com.micrantha.skouter.ui.arch

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.micrantha.skouter.ui.arch.i18n.ModelStatusFailure
import com.micrantha.skouter.ui.arch.i18n.NetworkFailure

fun Throwable.toi18n() = when (this) {
    is ApolloNetworkException -> NetworkFailure
    else -> ModelStatusFailure
}
