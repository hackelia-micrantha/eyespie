package com.micrantha.bluebell.domain.i18n

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.domain.usecase.FormatDateTimeUseCase
import com.micrantha.bluebell.ui.screen.LocalScreenContext
import kotlinx.datetime.Instant
import org.kodein.di.instance

interface LocalizedRepository {
    fun resource(str: LocalizedString, vararg args: Any?): String

    fun string(str: LocalizedString) = resource(str)

    fun format(epochSeconds: Long, format: String, timeZone: String, locale: String): String
}

@Composable
fun stringResource(str: LocalizedString) =
    LocalScreenContext.current.i18n.resource(str)

@Composable
fun longDateTime(instant: Instant): String {
    val viewContext = LocalScreenContext.current

    val formatDateTimeUseCase: FormatDateTimeUseCase by viewContext.instance()

    return formatDateTimeUseCase(instant)
}
