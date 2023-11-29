package com.micrantha.bluebell.platform

import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import okio.Path

expect class Platform : LocalizedRepository, FileSystem {
    val name: String

    override fun resource(str: LocalizedString, vararg args: Any?): String

    override fun format(
        epochSeconds: Long,
        format: String,
        timeZone: String,
        locale: String
    ): String

    override fun write(path: Path, data: ByteArray)

    override fun read(path: Path): ByteArray
}
