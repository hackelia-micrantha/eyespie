package com.micrantha.skouter

import android.content.Context
import androidx.activity.result.ActivityResultCaller
import com.micrantha.bluebell.Platform
import com.micrantha.skouter.data.service.LocationManager
import com.micrantha.skouter.data.service.PermissionManager
import org.kodein.di.DI
import org.kodein.di.bindInstance
import org.kodein.di.bindSingleton
import org.kodein.di.instance

fun androidDependencies(context: Context, activityResultCaller: ActivityResultCaller) = DI {
    bindInstance { context }

    bindSingleton { Platform(context) }

    bindSingleton { PermissionManager(activityResultCaller, instance()) }

    bindSingleton { LocationManager(context, instance(), instance()) }
}
