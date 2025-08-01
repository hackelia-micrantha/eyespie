package com.micrantha.bluebell.platform

import com.micrantha.bluebell.app.LocalNotifier
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

actual class Notifications : LocalNotifier {
    init {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound
        ) { granted, _ -> println("iOS permission granted: $granted") }
    }

    actual override fun schedule(id: String, title: String, message: String, atMillis: Long?) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
        }

        val trigger = atMillis?.let {
            val interval = (it - (NSDate().timeIntervalSince1970 * 1000)) / 1000
            if (interval > 0) UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
                interval, false
            ) else null
        }

        val request = UNNotificationRequest.requestWithIdentifier(id, content, trigger)
        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request, null)
    }

    actual override fun cancel(id: String) {
        UNUserNotificationCenter.currentNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(listOf(id))
    }
}
