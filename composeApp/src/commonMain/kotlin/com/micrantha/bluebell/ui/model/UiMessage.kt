package com.micrantha.bluebell.ui.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.benasher44.uuid.uuid4
import com.micrantha.bluebell.ui.model.UiMessage.Category.Default
import com.micrantha.bluebell.ui.model.UiMessage.Type.Timed
import org.jetbrains.compose.resources.StringResource

data class UiMessage(
    val id: String = uuid4().toString(),
    val message: StringResource,
    val category: Category = Default,
    val type: Type = Timed()
) {
    enum class Category {
        Default, Success, Info, Warning, Danger
    }

    sealed interface Type {
        data object Inline : Type

        data class Timed(val time: Int = LONG, val action: Action? = null) : Type {
            companion object {
                const val LONG = 3000
            }
        }

        data class Banner(val action: Action? = null) : Type

        data class Popup(
            val title: String? = null,
            val positive: Action? = null,
            val negative: Action? = null
        ) : Type

        data class FullScreen(
            val image: ImageVector,
            val title: String,
            val subtitle: String,
            val actions: List<Action>? = null,
        ) : Type
    }

    data class Action(
        val label: StringResource,
        val onClick: () -> Unit
    )
}
