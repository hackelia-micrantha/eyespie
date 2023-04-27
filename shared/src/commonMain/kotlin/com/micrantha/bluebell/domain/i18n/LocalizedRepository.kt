package com.micrantha.bluebell.domain.i18n

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.ui.view.LocalViewContext

interface LocalizedRepository {
    fun resource(str: LocalizedString): String
}

typealias StringResource = (LocalizedString) -> String

@Composable
fun stringResource(str: LocalizedString) =
    LocalViewContext.current.resource(str)
