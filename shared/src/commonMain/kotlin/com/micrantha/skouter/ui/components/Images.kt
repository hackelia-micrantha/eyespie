package com.micrantha.skouter.ui.components

import com.micrantha.bluebell.domain.model.UiResult.Failure
import com.micrantha.bluebell.domain.model.UiResult.Ready
import com.micrantha.bluebell.ui.toPainter
import com.micrantha.skouter.domain.models.Thing
import com.micrantha.skouter.domain.repository.StorageRepository


fun StorageRepository.image(thing: Thing.Listing) = get(thing.image).fold(
    onSuccess = { Ready(it.toPainter()) },
    onFailure = { Failure() }
)
