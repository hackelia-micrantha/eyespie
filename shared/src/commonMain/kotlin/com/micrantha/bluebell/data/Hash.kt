package com.micrantha.bluebell.data

import io.ktor.utils.io.core.*
import okio.Buffer
import okio.HashingSink.Companion.sha256

fun hash(input: String) = sha256(Buffer().write(input.toByteArray())).hash.base64()
