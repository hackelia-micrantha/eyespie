package com.micrantha.skouter

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.BluebellApp
import com.micrantha.skouter.ui.navi.routes
import org.koin.core.Koin

@Composable
fun App(koin: Koin) = BluebellApp(koin, routes())
