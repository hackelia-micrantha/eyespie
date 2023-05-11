package com.micrantha.bluebell

import androidx.compose.runtime.Composable
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import org.koin.core.component.KoinComponent

expect class Platform : LocalizedRepository, KoinComponent {
    val name: String
    override fun resource(str: LocalizedString, vararg args: Any?): String

    override fun format(
        epochSeconds: Long,
        format: String,
        timeZone: String,
        locale: String
    ): String
}

@Composable
expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)
