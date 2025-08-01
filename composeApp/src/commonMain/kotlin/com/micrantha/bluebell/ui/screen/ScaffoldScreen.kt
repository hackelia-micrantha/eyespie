package com.micrantha.bluebell.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.app.ScaffoldingState
import com.micrantha.bluebell.app.navi.NavAction
import com.micrantha.bluebell.ui.components.rememberConnectivityStatus
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.core.ui.navi.NavigationAction
import org.kodein.di.DIAware

internal fun defaultBackAction() = NavAction(
    icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    action = { context -> context.router.navigateBack() }
)

class ScaffoldScreenModel(context: ScreenContext) : StatefulScreenModel<ScaffoldingState>(
    context,
    ScaffoldingState()
)

abstract class ScaffoldScreen(
    private val context: ScreenContext
): Screen, DIAware by context {

    @Composable
    abstract fun Render()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val context = LocalScreenContext.current

        val connectivityStatus by rememberConnectivityStatus()

        val screenModel: ScaffoldScreenModel = rememberScreenModel()

        val scaffold by screenModel.state.collectAsState()

        Scaffold(
            floatingActionButton = {
                scaffold.floatingActionButton?.let {
                    NavigationAction(
                        navAction = it
                    )
                }
            },
            topBar = {
                TopAppBar(
                    modifier = Modifier.shadow(Dimensions.content),
                    title = {
                        scaffold.title?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    },
                    navigationIcon = {
                        if (scaffold.showBack) {
                            scaffold.backAction?.let {
                                NavigationAction(
                                    navAction = it
                                )
                            } ?: run {
                                if ((scaffold.canGoBack == null && context.router.canGoBack) || scaffold.canGoBack == true) {
                                    NavigationAction(
                                        navAction = defaultBackAction()
                                    )
                                }
                            }
                        }
                    },
                    actions = {
                        scaffold.actions?.forEach { nav ->
                            NavigationAction(
                                modifier = Modifier.padding(start = Dimensions.content),
                                navAction = nav
                            )
                        }
                    }
                )
                if (connectivityStatus.not()) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(Dimensions.screen).background(
                            Color(255, 103, 0)
                        ), text = "No network connection available"
                    )
                }
            }
        ) { offsets ->
            Surface(
                modifier = Modifier.padding(
                    top = offsets.calculateTopPadding(),
                    bottom = offsets.calculateBottomPadding()
                )
            ) {
                Render()
            }
        }
    }
}
