package com.micrantha.bluebell.ui.components.message

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.micrantha.bluebell.domain.entities.LocalizedString
import com.micrantha.bluebell.ui.model.UiMessage
import com.micrantha.bluebell.ui.model.UiMessage.Action
import com.micrantha.bluebell.ui.model.UiMessage.Category
import com.micrantha.bluebell.ui.model.UiMessage.Category.Danger
import com.micrantha.bluebell.ui.model.UiMessage.Category.Default
import com.micrantha.bluebell.ui.model.UiMessage.Category.Info
import com.micrantha.bluebell.ui.model.UiMessage.Category.Success
import com.micrantha.bluebell.ui.model.UiMessage.Category.Warning
import com.micrantha.bluebell.ui.model.UiMessage.Type.Popup
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.eyespie.app.S
import eyespie.composeapp.generated.resources.ok


val Category.icon: ImageVector?
    get() = when (this) {
        Success -> Icons.Default.CheckCircle
        Danger -> Icons.Default.Error
        Warning -> Icons.Default.Warning
        Info -> Icons.Default.Info
        else -> null
    }

fun ScreenContext.popup(
    message: LocalizedString,
    category: Category = Default,
    accept: () -> Unit
) =
    UiMessage(
        message = message,
        category = category,
        type = Popup(
            positive = Action(
                S.ok,
                accept
            )
        )
    )
