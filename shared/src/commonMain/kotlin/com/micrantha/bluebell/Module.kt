package com.micrantha.bluebell

import com.micrantha.bluebell.domain.bluebellDomain
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.bluebell.platform.Platform
import com.micrantha.bluebell.ui.bluebellUi
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.DITrigger
import org.kodein.di.DirectDIAware
import org.kodein.di.delegate
import org.kodein.di.instance

fun bluebellModules(trigger: DITrigger? = null) = DI.Module(name = "Bluebell") {

    importOnce(bluebellDomain())
    importOnce(bluebellUi())

    delegate<LocalizedRepository>().to<Platform>()
    delegate<FileSystem>().to<Platform>()

    onReady {
        trigger?.trigger()
    }
}

inline fun <reified T : Any> DIAware.get() = instance<T>()
inline fun <reified T : Any> DirectDIAware.get() = instance<T>()
