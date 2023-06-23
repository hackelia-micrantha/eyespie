package com.micrantha.bluebell.data

import io.ktor.utils.io.core.toByteArray
import okio.Buffer
import okio.HashingSink.Companion.sha256
import okio.use

fun hash(input: String): String = Buffer().use {
    it.write(input.toByteArray()).flush()
    sha256(it).hash.base64Url()
}
