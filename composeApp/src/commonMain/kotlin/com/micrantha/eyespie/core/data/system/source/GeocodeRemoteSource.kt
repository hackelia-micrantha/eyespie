package com.micrantha.eyespie.core.data.system.source

import com.micrantha.eyespie.core.data.client.createHttpClient
import io.ktor.client.request.get
import io.ktor.http.URLProtocol

class GeocodeRemoteSource {
    private val client by lazy {
        createHttpClient {
            url {
                protocol = URLProtocol.HTTPS
                host = "nominatim.openstreetmap.org"
                parameters.append("format", "jsonv2")
            }
        }
    }

    suspend fun reverse(lat: Double, lon: Double) =
        client.get("/reverse") {
            url {
                parameters.append("lat", lat.toString())
                parameters.append("lon", lon.toString())
            }
        }
}
