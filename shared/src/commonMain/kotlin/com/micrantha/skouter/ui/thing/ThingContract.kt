package com.micrantha.skouter.ui.thing

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.skouter.domain.model.Image

sealed class ThingAction : Action {

    data class DownloadImage(val data: Image) : ThingAction()
}
