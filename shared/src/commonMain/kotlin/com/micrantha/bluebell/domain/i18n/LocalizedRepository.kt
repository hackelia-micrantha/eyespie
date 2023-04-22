package com.micrantha.bluebell.domain.i18n

interface LocalizedRepository {
    fun resource(str: LocalizedString): String
}
