package com.micrantha.skouter

import android.content.Context
import androidx.compose.runtime.Composable
import com.micrantha.bluebell.Platform
import com.micrantha.bluebell.bluebellModules
import org.koin.compose.getKoin
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(context: Context) {

    startKoin {
        modules(module {
            single { Platform(context) }
        }, bluebellModules(), skouterModules())
    }
}

@Composable
fun UIShow() = App(getKoin())
