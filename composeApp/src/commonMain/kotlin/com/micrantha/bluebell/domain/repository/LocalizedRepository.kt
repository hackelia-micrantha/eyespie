package com.micrantha.bluebell.domain.repository

import com.micrantha.bluebell.domain.entities.LocalizedString
import org.jetbrains.compose.resources.getString

interface LocalizedRepository {
    suspend fun string(str: LocalizedString): String = getString(str)

    suspend fun string(str: LocalizedString, vararg args: Any): String = getString(str, *args)

    fun format(epochSeconds: Long, format: String, timeZone: String, locale: String): String
}
