package com.micrantha.eyespie.core.data.client

import com.apollographql.apollo.ApolloCall
import com.micrantha.eyespie.app.AppConfig
import com.micrantha.eyespie.features.things.data.model.MatchRequest
import com.micrantha.eyespie.features.things.data.model.NearbyRequest
import com.micrantha.eyespie.graphql.GameListQuery
import com.micrantha.eyespie.graphql.GameNodeQuery
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.graphql.GraphQL
import io.github.jan.supabase.graphql.graphql
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.PostgrestQueryBuilder
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

typealias AuthClient = Auth
typealias DatabaseClient = Postgrest
typealias StorageClient = Storage
typealias GraphClient = GraphQL
typealias RealtimeClient = Realtime

typealias DatabaseCall = PostgrestQueryBuilder
typealias GraphCall<T> = ApolloCall<T>
typealias StorageCall = BucketApi
typealias AuthCall = Auth

class SupaClient {
    private val supabase by lazy {
        createSupabaseClient(AppConfig.SUPABASE_URL, AppConfig.SUPABASE_KEY) {
            install(GraphClient)

            install(DatabaseClient)

            install(AuthClient) {
                flowType = FlowType.PKCE
            }

            install(StorageClient)
        }
    }

    fun auth(): AuthCall = supabase.auth

    fun players(): DatabaseCall = supabase.postgrest["Player"]

    fun games(): GraphCall<GameListQuery.Data> =
        supabase.graphql.apolloClient.query(GameListQuery())

    fun game(id: String): GraphCall<GameNodeQuery.Data> =
        supabase.graphql.apolloClient.query(GameNodeQuery(id))

    fun things() = supabase.postgrest["Thing"]

    fun storage(bucketId: String): StorageCall = supabase.storage[bucketId]

    suspend fun nearby(request: NearbyRequest) = supabase.postgrest.rpc(
        function = "thingsnearby",
        parameters = request
    )

    suspend fun match(request: MatchRequest) = supabase.postgrest.rpc(
        function = "match_things",
        parameters = request
    )
}
