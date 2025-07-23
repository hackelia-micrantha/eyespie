package com.micrantha.bluebell.ui.model

data class TextEntryState(
    val value: String = "",
    val error: String? = null
) {
    val isError = error != null
}
