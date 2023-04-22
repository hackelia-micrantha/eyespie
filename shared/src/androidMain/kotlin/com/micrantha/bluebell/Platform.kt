package com.micrantha.bluebell

import android.content.Context
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString

actual class Platform(private val context: Context) : LocalizedRepository {
    actual val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    actual override fun resource(str: LocalizedString): String {
        val key = str.androidKey ?: str.key
        val id = context.resources.getIdentifier(key, "string", context.packageName)
        return context.getString(id)
    }
}
