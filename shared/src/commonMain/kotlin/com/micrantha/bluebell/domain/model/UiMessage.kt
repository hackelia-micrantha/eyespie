package com.micrantha.bluebell.domain.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.benasher44.uuid.uuid4
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.i18n.LocalizedString
import com.micrantha.bluebell.domain.model.UiMessage.Category
import com.micrantha.bluebell.domain.model.UiMessage.Category.Default
import com.micrantha.bluebell.domain.model.UiMessage.Type.Popup
import com.micrantha.bluebell.domain.model.UiMessage.Type.Timed
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.skouter.ui.component.S

data class UiMessage(
    val id: String = uuid4().toString(),
    val message: String,
    val actions: List<Action>? = null,
    val category: Category = Default,
    val type: Type = Timed()
) : Action {
    enum class Category {
        Default, Success, Info, Warning, Danger
    }

    sealed interface Type {
        object Inline : Type

        data class Timed(val time: Int = LONG) : Type {
            companion object {
                const val LONG = 3000
            }
        }

        object Banner : Type

        object Popup : Type

        data class FullScreen(
            val image: ImageVector,
            val title: String,
            val subtitle: String
        ) : Type
    }

    data class Action(
        val label: String,
        val onClick: () -> Unit
    )
}

fun ScreenContext.popup(
    message: LocalizedString,
    category: Category = Default,
    accept: () -> Unit
) =
    UiMessage(
        message = i18n.string(message),
        actions = listOf(
            UiMessage.Action(
                i18n.string(S.OK),
                accept
            ),
        ),
        category = category,
        type = Popup
    )
