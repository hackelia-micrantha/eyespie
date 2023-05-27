package com.micrantha.bluebell

import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import okio.FileSystem
import okio.Path
import okio.buffer
import okio.use
import platform.Foundation.NSLocalizedString
import platform.UIKit.UIDevice
import com.micrantha.bluebell.FileSystem as BluebellFileSystem

actual class Platform : LocalizedRepository, BluebellFileSystem {
    actual val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    actual override fun resource(str: LocalizedString, vararg args: Any?): String {
        return NSLocalizedString(str.iosKey ?: str.key, *args)
    }

    actual override fun format(
        epochSeconds: Long,
        format: String,
        timeZone: String,
        locale: String
    ): String {
        val date = NSDate.dateWithTimeIntervalFrom1970(epochSeconds)
        val dateFormatter = NSDateFormatter()
        dateFormatter.timeZone = NSTimeZone(timeZone)
        dateFormatter.locale = NSLocale(locale)
        dateFormatter.dateFormat = format
        return dateFormatter.stringFromDate(date)
    }

    actual override fun write(path: Path, data: ByteArray) {
        FileSystem.SYSTEM.sink(path, true).use { sink ->
            sink.buffer().use { buf ->
                buf.write(data)
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

actual class WeakReference<out T : Any> actual constructor(target: T) {
    private val underlying: kotlin.native.ref.WeakReference<T> =
        kotlin.native.ref.WeakReference(target)
    actual val targetOrNull: T? get() = underlying.get()
}
