package com.micrantha.bluebell

import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import okio.Path

interface FileSystem {

    fun write(path: Path, data: ByteArray)

    fun read(path: Path): ByteArray
}

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

expect class WeakReference<out T : Any>(target: T) {
    val targetOrNull: T?
}
