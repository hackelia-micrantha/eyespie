package com.micrantha.skouter.data.client

import Skouter.shared.BuildConfig
import com.apollographql.apollo3.ApolloCall
import com.micrantha.skouter.data.thing.model.NearbyRequest
import com.micrantha.skouter.graphql.GameListQuery
import com.micrantha.skouter.graphql.GameNodeQuery
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.graphql.GraphQL
import io.github.jan.supabase.graphql.graphql
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.PostgrestBuilder
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

typealias AuthClient = GoTrue
typealias DatabaseClient = Postgrest
typealias StorageClient = Storage
typealias GraphClient = GraphQL
typealias RealtimeClient = Realtime

typealias DatabaseCall = PostgrestBuilder
typealias GraphCall<T> = ApolloCall<T>
typealias StorageCall = BucketApi
typealias AuthCall = GoTrue

class SupaClient {
    private val supabase =
        createSupabaseClient("https://${BuildConfig.supaBaseDomain}", BuildConfig.supaBaseKey) {
            install(GraphClient)

            install(DatabaseClient)

            install(AuthClient)

            install(StorageClient)
        }

    fun auth(): AuthCall = supabase.gotrue

    fun players(): DatabaseCall = supabase.postgrest["Player"]

    fun games(): GraphCall<GameListQuery.Data> =
        supabase.graphql.apolloClient.query(GameListQuery())

    fun game(id: String): GraphCall<GameNodeQuery.Data> =
        supabase.graphql.apolloClient.query(GameNodeQuery(id))

    fun things() = supabase.postgrest["Thing"]

    fun storage(bucketId: String): StorageCall = supabase.storage[bucketId]

    suspend fun nearby(request: NearbyRequest) = supabase.postgrest.rpc(
        function = "thingsNearby",
        parameters = request
    )
}
