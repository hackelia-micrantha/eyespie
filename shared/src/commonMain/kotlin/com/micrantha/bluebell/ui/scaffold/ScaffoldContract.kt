package com.micrantha.bluebell.ui.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.navi.NavAction

data class ScaffoldState(
    val title: String? = null,
    val showBack: Boolean = false,
    val backAction: NavAction? = null,
    val actions: List<NavAction>? = null
)

sealed class ScaffoldAction : Action {
    data class SetTitle(val title: String) : ScaffoldAction()

    data class SetNavigation(val builder: ScaffoldBuilder) : ScaffoldAction()

    object Reset : ScaffoldAction()

    companion object {
        fun navigation(init: ScaffoldBuilder.() -> Unit) =
            ScaffoldBuilder().apply(init).build()
    }
}

@Composable
fun rememberScaffoldState(viewContext: ViewContext): State<ScaffoldState> {
    val store = remember {
        viewContext.createStore(ScaffoldState())
            .addReducer(::scaffoldReducer)
    }
    return store.state().collectAsState()
}
