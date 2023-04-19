package com.micrantha.skouter.data.remote

import com.apollographql.apollo3.ApolloClient
import com.micrantha.skouter.AnonymousSessionMutation
import com.micrantha.skouter.GameListQuery
import com.micrantha.skouter.GetAccountQuery

class ApiClient {
    private val client = ApolloClient.Builder()
        .serverUrl("http://localhost/v1/graphql")
        .build()

     fun loginAnonymous() = client.mutation(AnonymousSessionMutation())

     fun account() = client.query(GetAccountQuery())

     fun games(databaseId: String = "643f999cec48757ab51e") = client.query(GameListQuery(databaseId))
}
