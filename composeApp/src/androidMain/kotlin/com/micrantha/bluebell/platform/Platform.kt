package com.micrantha.bluebell.platform

import android.content.Context
import com.micrantha.bluebell.domain.entities.LocalizedString
import com.micrantha.bluebell.domain.repository.LocalizedRepository
import okio.FileSystem
import okio.Path
import okio.buffer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.micrantha.bluebell.platform.FileSystem as BluebellFileSystem


actual class Platform(
    private val context: Context,
    actual val networkMonitor: NetworkMonitor
) : LocalizedRepository, BluebellFileSystem {
    actual val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    actual override fun string(str: LocalizedString): String {
        return context.getString(context.resources.getIdentifier(str.key, "string", context.packageName))
    }

    actual override fun string(
        str: LocalizedString,
        vararg args: Any
    ): String {
        TODO("Not yet implemented")
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
            val formatter = DateTimeFormatter.ofPattern(format, Locale.forLanguageTag(locale))
        return date.format(formatter)
    }

    actual override fun write(path: Path, data: ByteArray) {
        FileSystem.SYSTEM.sink(path).use { sink ->
            sink.buffer().use { buf ->
                buf.write(data)
                buf.flush()
            }
        }
    }

    actual override fun read(path: Path): ByteArray {
        return FileSystem.SYSTEM.source(path).use { src ->
            src.buffer().use { buf ->
                buf.readByteArray()
            }
        }
    }
}
