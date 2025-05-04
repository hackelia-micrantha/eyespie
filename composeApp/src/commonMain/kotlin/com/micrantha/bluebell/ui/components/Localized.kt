package com.micrantha.bluebell.ui.components

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.domain.entities.LocalizedString
import com.micrantha.bluebell.domain.usecase.FormatDateTimeUseCase
import com.micrantha.bluebell.ui.screen.LocalScreenContext
import kotlinx.datetime.Instant
import org.kodein.di.instance

@Composable
fun stringResource(str: LocalizedString) =
    LocalScreenContext.current.i18n.resource(str)

@Composable
fun longDateTime(instant: Instant): String {
    val viewContext = LocalScreenContext.current

    val formatDateTimeUseCase: FormatDateTimeUseCase by viewContext.instance()

    return formatDateTimeUseCase(instant)
}
