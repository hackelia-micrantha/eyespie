package com.micrantha.bluebell.platform

import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import okio.FileSystem
import okio.Path
import okio.buffer
import okio.use
import platform.Foundation.NSBundle
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeZoneWithName
import platform.UIKit.UIDevice
import platform.UIKit.UIViewController
import com.micrantha.bluebell.platform.FileSystem as BluebellFileSystem

actual class Platform(
    private val viewController: UIViewController
) : LocalizedRepository, BluebellFileSystem {

    val view = viewController.view
    
    actual val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    actual override fun resource(str: LocalizedString, vararg args: Any?): String {
        val format =
            NSBundle.mainBundle.localizedStringForKey(str.iosKey ?: str.key, str.toString(), null)
        /*if (args.isNotEmpty()) {
            return NSString.stringWithFormat(format, *arrayOf(*arg))
        }*/
        return format
    }

    actual override fun format(
        epochSeconds: Long,
        format: String,
        timeZone: String,
        locale: String
    ): String {
        val date = NSDate.dateWithTimeIntervalSince1970(epochSeconds.toDouble())
        val dateFormatter = NSDateFormatter()
        dateFormatter.timeZone = NSTimeZone.timeZoneWithName(timeZone)!!
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
