package com.micrantha.skouter.data.remote

import Skouter.shared.BuildConfig
import com.micrantha.skouter.GameListQuery
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.graphql.GraphQL
import io.github.jan.supabase.graphql.graphql
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

typealias AuthClient = GoTrue
typealias DatabaseClient = Postgrest

class ApiClient {
    private val supabase =
        createSupabaseClient("https://${BuildConfig.apiDomain}", BuildConfig.apiKey) {
            install(GraphQL)

            install(Postgrest)

            install(GoTrue)
        }

    init {
        Napier.base(DebugAntilog())
    }

    fun auth(): AuthClient = supabase.gotrue

    fun players() = supabase.postgrest["players"]

    fun games() = supabase.graphql.apolloClient.query(GameListQuery())

}
