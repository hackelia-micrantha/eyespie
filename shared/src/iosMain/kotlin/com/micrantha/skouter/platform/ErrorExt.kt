package com.micrantha.skouter.platform

import platform.Foundation.NSError


fun NSError.asException() = Exception("${this.code} - ${this.localizedDescription}")
