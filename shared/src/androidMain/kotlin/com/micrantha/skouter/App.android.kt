package com.micrantha.skouter

import android.content.Context
import androidx.compose.runtime.Composable
import com.micrantha.bluebell.bluebellModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.getKoin
import org.koin.core.context.startKoin

fun initKoin(context: Context) = startKoin {
    androidLogger()
    androidContext(context)
    modules(
        androidModules(),
        bluebellModules(),
        skouterModules()
    )
}

@Composable
fun UIShow() {
    SkouterApp(getKoin().get())
}
