package com.micrantha.bluebell.domain.i18n

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.domain.usecase.FormatDateTimeUseCase
import com.micrantha.bluebell.ui.view.LocalViewContext
import kotlinx.datetime.Instant
import org.koin.core.component.inject

interface LocalizedRepository {
    fun resource(str: LocalizedString): String

    fun format(epochSeconds: Long, format: String, timeZone: String, locale: String): String
}

typealias StringResource = (LocalizedString) -> String

@Composable
fun stringResource(str: LocalizedString) =
    LocalViewContext.current.resource(str)

@Composable
fun longDateTime(instant: Instant): String {
    val viewContext = LocalViewContext.current

    val formatDateTimeUseCase: FormatDateTimeUseCase by viewContext.inject()

    return formatDateTimeUseCase(instant)
}
