package com.micrantha.bluebell

import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString

expect class Platform : LocalizedRepository {
    val name: String
    override fun resource(str: LocalizedString, vararg args: Any?): String

    override fun format(
        epochSeconds: Long,
        format: String,
        timeZone: String,
        locale: String
    ): String
}
