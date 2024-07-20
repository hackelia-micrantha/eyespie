package com.micrantha.eyespie.platform

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.*

actual fun httpClientEngine(): HttpClientEngine = CIO.create {

}
