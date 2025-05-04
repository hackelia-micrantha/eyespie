package com.micrantha.bluebell.domain.entities

interface LocalizedString {
    val key: String
    val androidKey: String?
    val iosKey: String?
}