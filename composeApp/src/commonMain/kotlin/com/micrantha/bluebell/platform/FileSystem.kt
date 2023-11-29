package com.micrantha.bluebell.platform

import okio.Path

interface FileSystem {

    fun write(path: Path, data: ByteArray)

    fun read(path: Path): ByteArray
}
