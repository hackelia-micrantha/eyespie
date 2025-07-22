package com.micrantha.bluebell.domain.usecase

import androidx.compose.ui.text.intl.Locale
import com.micrantha.bluebell.platform.Platform
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlin.time.ExperimentalTime

class FormatDateTimeUseCase(private val platform: Platform) {
    @OptIn(ExperimentalTime::class)
    operator fun invoke(instant: Instant): String {
        // TODO: get locale from user prefs
        val locale = Locale.current
        // TODO: get time zone from user prefs
        val zone = TimeZone.currentSystemDefault()

        // TODO: minimize format based upon duration to now
        return platform.format(
            instant.epochSeconds,
            DATE_TIME_LONG,
            zone.id,
            locale.toLanguageTag()
        )
    }

    companion object {
        const val DATE_TIME_LONG = "MMM dd yyyy 'at' hh:mm a"
    }
}
