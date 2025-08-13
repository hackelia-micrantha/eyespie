package com.micrantha.bluebell.domain.repository

import com.micrantha.bluebell.domain.entities.LocalizedString
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString

interface LocalizedRepository {
     fun string(str: LocalizedString) = runCatching {
        runBlocking { getString(str) }
    }

    fun string(str: LocalizedString, vararg args: Any) = runCatching {
        runBlocking {  getString(str, *args) }
    }


    fun format(epochSeconds: Long, format: String, timeZone: String, locale: String): String
}
