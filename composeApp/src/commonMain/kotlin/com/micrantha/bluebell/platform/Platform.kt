package com.micrantha.bluebell.platform

import com.micrantha.bluebell.domain.repository.LocalizedRepository
import okio.Path

expect class Platform : LocalizedRepository, FileSystem {
    val name: String

    val networkMonitor: NetworkMonitor

    override fun format(
        epochSeconds: Long,
        format: String,
        timeZone: String,
        locale: String
    ): String

    override fun write(path: Path, data: ByteArray)

    override fun read(path: Path): ByteArray
}
