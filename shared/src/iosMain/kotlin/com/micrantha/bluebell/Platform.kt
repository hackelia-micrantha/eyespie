package com.micrantha.bluebell

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import org.koin.core.component.KoinComponent
import platform.Foundation.NSLocalizedString
import platform.UIKit.UIDevice

actual class Platform : LocalizedRepository, KoinComponent {
    actual val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    actual override fun resource(str: LocalizedString, vararg args: Any?): String {
        return NSLocalizedString(str.iosKey ?: str.key, *args)
    }

    actual fun format(
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
}


@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) = Unit
