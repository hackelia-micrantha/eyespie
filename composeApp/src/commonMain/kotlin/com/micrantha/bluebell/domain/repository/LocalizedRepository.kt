package com.micrantha.bluebell.domain.repository

import com.micrantha.bluebell.domain.entities.LocalizedString
import org.jetbrains.compose.resources.getString

interface LocalizedRepository {
    suspend fun resource(str: LocalizedString) = getString(str)

    suspend fun resource(str: LocalizedString, vararg args: Any) = getString(str, *args)

    fun string(str: LocalizedString): String

    fun string(str: LocalizedString, vararg args: Any): String

    fun format(epochSeconds: Long, format: String, timeZone: String, locale: String): String
}
