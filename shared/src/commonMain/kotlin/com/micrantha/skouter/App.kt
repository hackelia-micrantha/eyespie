
package com.micrantha.skouter

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.micrantha.bluebell.ui.MainAction
import com.micrantha.bluebell.ui.MainScreen
import com.micrantha.bluebell.ui.MainViewModel
import com.micrantha.bluebell.ui.Modules
import com.micrantha.bluebell.ui.loadUiModules
import com.micrantha.bluebell.ui.navi.Route
import com.micrantha.skouter.presentation.screens.games.GameListScreen
import com.micrantha.skouter.presentation.screens.games.GameListViewModel
import org.koin.core.component.get

enum class AppRoutes(override val path: String, override val isDefault: Boolean = false) : Route {
    Games("games", true),
    NewGame("games/new")
}

@Composable
internal fun App() {
    loadUiModules()

    val viewModel = Modules.get<MainViewModel>()

    viewModel.dispatch(MainAction.setRoutes {
        AppRoutes.Games to { viewModel: GameListViewModel ->
            GameListScreen(viewModel)
        }
    })

    Surface( color = MaterialTheme.colorScheme.background ) {
       MainScreen(viewModel)
    }
}
