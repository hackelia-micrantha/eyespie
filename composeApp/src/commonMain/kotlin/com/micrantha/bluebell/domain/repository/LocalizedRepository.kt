package com.micrantha.bluebell.domain.repository

import com.micrantha.bluebell.domain.entities.LocalizedString

interface LocalizedRepository {
    fun resource(str: LocalizedString, vararg args: Any?): String

    fun string(str: LocalizedString) = resource(str)

    fun format(epochSeconds: Long, format: String, timeZone: String, locale: String): String
}
