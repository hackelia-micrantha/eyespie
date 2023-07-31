package com.micrantha.skouter.platform

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

actual fun httpClientEngine(): HttpClientEngine = Android.create {

}
