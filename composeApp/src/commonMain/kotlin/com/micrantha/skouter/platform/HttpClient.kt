package com.micrantha.skouter.platform

import io.ktor.client.engine.HttpClientEngine

expect fun httpClientEngine(): HttpClientEngine
