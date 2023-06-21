package com.micrantha.skouter.data.system.source

import com.micrantha.skouter.data.client.createHttpClient
import io.ktor.client.request.*
import io.ktor.http.*

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
