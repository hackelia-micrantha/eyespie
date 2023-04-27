package com.micrantha.skouter.android

import android.app.Application
import com.micrantha.skouter.initKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        initKoin(this)
    }
}
