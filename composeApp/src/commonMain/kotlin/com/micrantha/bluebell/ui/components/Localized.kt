package com.micrantha.bluebell.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import com.micrantha.bluebell.domain.entities.LocalizedString
import com.micrantha.bluebell.domain.usecase.FormatDateTimeUseCase
import com.micrantha.bluebell.ui.screen.LocalScreenContext
import org.jetbrains.compose.resources.getString
import org.kodein.di.instance
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
fun longDateTime(instant: Instant): String {
    val viewContext = LocalScreenContext.current

    val formatDateTimeUseCase: FormatDateTimeUseCase by viewContext.instance()

    return formatDateTimeUseCase(instant)
}

@Composable
fun stringState(str: LocalizedString): State<String> {
    return produceState(initialValue = "") {
        value = getString(str)
    }
}
