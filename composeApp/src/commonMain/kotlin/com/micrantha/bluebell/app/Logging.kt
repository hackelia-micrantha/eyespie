package com.micrantha.bluebell.app

import co.touchlab.kermit.Logger

typealias Log = Logger

fun Log.d(tag: String, message: String) = Log.d(tag) { message }
