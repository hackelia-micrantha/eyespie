package com.micrantha.bluebell

import android.content.Context
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

actual class Platform(private val context: Context) : LocalizedRepository {
    actual val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    actual override fun resource(str: LocalizedString, vararg args: Any?): String {
        val key = str.androidKey ?: str.key
        val id = context.resources.getIdentifier(key, "string", context.packageName)
        return context.getString(id, *args)
    }

    actual override fun format(
        epochSeconds: Long,
        format: String,
        timeZone: String,
        locale: String
    ): String {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val zoneId = ZoneId.of(timeZone)
        val date = LocalDateTime.ofInstant(instant, zoneId)
        val formatter = DateTimeFormatter.ofPattern(format, Locale(locale))
        return date.format(formatter)
    }
}
