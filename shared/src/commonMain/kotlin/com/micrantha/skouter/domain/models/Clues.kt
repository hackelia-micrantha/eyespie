package com.micrantha.skouter.domain.models

data class Clues(
    val color: String? = null,
    val who: String? = null,
    val what: String? = null,
    val where: Location? = null,
    val why: String? = null,
    val rhyme: String? = null
)
